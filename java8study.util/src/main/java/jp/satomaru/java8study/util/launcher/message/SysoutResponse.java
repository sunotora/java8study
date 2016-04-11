package jp.satomaru.java8study.util.launcher.message;

import jp.satomaru.java8study.util.launcher.validate.InvalidArgument;
import jp.satomaru.java8study.util.launcher.validate.InvalidParameter;

/**
 * 実行するオブジェクトからの応答を、標準出力に出力します。
 */
public class SysoutResponse implements Response {

	/**
	 * 値の文字列表現を、標準出力に出力します。
	 * 
	 * <p>
	 * nullの場合は"null"と出力されます。
	 * </p>
	 * 
	 * @param content 出力する値
	 */
	@Override
	public void output(Object content) {
		System.out.println(content);
	}

	/**
	 * 引数例外のメッセージを、標準出力に出力します。
	 * 
	 * @param invalid 引数例外
	 */
	@Override
	public void error(InvalidArgument invalid) {
		System.out.println(invalid.getMessage());
	}

	/**
	 * 名前つき引数例外のメッセージを、標準出力に出力します。
	 * 
	 * @param invalid 名前つき引数例外
	 */
	@Override
	public void error(InvalidParameter invalid) {
		System.out.println(invalid.getMessage());
	}

	/**
	 * レスポンスをクローズします。
	 * 
	 * <p>
	 * 実際には何も行いません。
	 * </p>
	 */
	@Override
	public void close() throws Exception {

	}
}
