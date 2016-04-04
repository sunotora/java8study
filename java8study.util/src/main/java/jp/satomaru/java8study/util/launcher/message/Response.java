package jp.satomaru.java8study.util.launcher.message;

import jp.satomaru.java8study.util.launcher.validate.InvalidArgument;
import jp.satomaru.java8study.util.launcher.validate.InvalidParameter;

/**
 * モデルからの応答です。
 */
public interface Response extends AutoCloseable {

	/**
	 * レスポンスに値を出力します。
	 * 
	 * @param content 出力する値
	 */
	void output(Object content);

	/**
	 * レスポンスにエラーを出力します。
	 * 
	 * @param invalid 引数例外
	 */
	void error(InvalidArgument invalid);

	/**
	 * レスポンスにエラーを出力します。
	 * 
	 * @param invalid 名前つき引数例外
	 */
	void error(InvalidParameter invalid);
}
