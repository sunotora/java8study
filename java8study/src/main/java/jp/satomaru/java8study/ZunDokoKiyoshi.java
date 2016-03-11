package jp.satomaru.java8study;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;

public class ZunDokoKiyoshi {

	@RequiredArgsConstructor
	public enum Word {
		ZUN("ズン"), DOKO("ドコ"), KIYOSHI("キ・ヨ・シ！");
		private final String string;

		@Override
		public String toString() {
			return string;
		}
	}

	public static final int ZUN_CHAIN_WHEN_FINISH = 4;

	public static void main(String[] args) {
		execute(System.out::print);
	}

	public static void execute(Consumer<Word> acceptor) {
		int zunChain = 0;
		boolean finish = false;

		do {
			Word word = (Math.random() < 0.5) ? Word.ZUN : Word.DOKO;
			acceptor.accept(word);

			if (word.equals(Word.ZUN)) {
				++zunChain;
			} else if (zunChain >= ZUN_CHAIN_WHEN_FINISH) {
				acceptor.accept(Word.KIYOSHI);
				finish = true;
			} else {
				zunChain = 0;
			}
		} while (!finish);
	}
}
