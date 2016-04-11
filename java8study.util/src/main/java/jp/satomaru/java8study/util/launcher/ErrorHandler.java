package jp.satomaru.java8study.util.launcher;

import jp.satomaru.java8study.util.launcher.message.Message;
import jp.satomaru.java8study.util.launcher.validate.InvalidArgument;
import jp.satomaru.java8study.util.launcher.validate.InvalidParameter;

/**
 * このインターフェースを実装すると、各メソッドがエラーハンドラーとして自動的にランチャーに設定されます。
 * 
 * <p>
 * {@link Launcher}に既に各エラーハンドラーが設定されている場合でも、この実装が優先されます。
 * </p>
 */
public interface ErrorHandler {

	/**
	 * コマンドが指定されていない場合に実行されます。
	 * 
	 * @param message メッセージ
	 */
	void whenNoCommand(Message message);

	/**
	 * 該当するコマンドが存在しない場合に実行されます。
	 * 
	 * @param message メッセージ
	 * @param command 指定されたコマンド
	 */
	void whenIllegalCommand(Message message, String command);

	/**
	 * インデックスに関連付けられた引数のバリデーション例外が発生した場合に実行されます。
	 * 
	 * @param message メッセージ
	 * @param invalid 発生した例外
	 */
	void whenInvalidArgument(Message message, InvalidArgument invalid);

	/**
	 * 名前に関連付けられた引数のバリデーション例外が発生した場合に実行されます。
	 * 
	 * @param message メッセージ
	 * @param invalid 発生した例外
	 */
	void whenInvalidParameter(Message message, InvalidParameter invalid);

	/**
	 * エラー処理後に実行されます。
	 * 
	 * @param message メッセージ
	 */
	void whenError(Message message);
}
