package jp.satomaru.java8study;

import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

/**
 * うわさのズンドコキヨシ関数です。
 */
public class ZunDokoKiyoshi {

	/** ワード。 */
	@RequiredArgsConstructor
	public enum Word {
		ZUN("ズン"), DOKO("ドコ"), KIYOSHI("キ・ヨ・シ！");

		private final String string;

		@Override
		public String toString() {
			return string;
		}
	}

	/** 終了条件のひとつである、「ズン」が連続する回数。 */
	public static final int ZUN_CHAIN_WHEN_FINISH = 4;

	/**
	 * メイン。
	 * 
	 * @param args 使用しません
	 */
	public static void main(String[] args) {
		Supplier<Word> generator = () -> (Math.random() < 0.5) ? Word.ZUN : Word.DOKO;
		Consumer<Word> acceptor = System.out::print;
		execute(generator, acceptor);
	}

	/**
	 * 終了条件を満たすまでワードの生成を繰り返し、その後、「キ・ヨ・シ！」を生成して終了します。
	 * 
	 * <p>
	 * 終了条件とは、生成された最新5ワードが「ズン」「ズン」「ズン」「ズン」「ドコ」という配列になることを指します。
	 * </p>
	 * 
	 * <p>
	 * なお、この関数は「速度の最適化」や「サイズの最適化」という観点ではなく、
	 * 「処理の純粋な抽出」および「可読性の向上」という観点によって設計されています。
	 * </p>
	 * 
	 * @param generator 「ズン」と「ドコ」を生成する関数
	 * @param acceptor 生成したワードを受け取る関数
	 */
	public static void execute(Supplier<Word> generator, Consumer<Word> acceptor) {
		int zunChain = 0;
		boolean finish = false;

		do {
			Word word = generator.get();

			switch (word) {
				case ZUN:
					++zunChain;
					break;

				case DOKO:
					finish = (zunChain >= ZUN_CHAIN_WHEN_FINISH);
					zunChain = 0;
					break;

				default:
					throw new IllegalStateException("「ズン」と「ドコ」以外のワードが生成されました: " + word);
			}

			acceptor.accept(word);
		} while (!finish);

		acceptor.accept(Word.KIYOSHI);
	}
}
