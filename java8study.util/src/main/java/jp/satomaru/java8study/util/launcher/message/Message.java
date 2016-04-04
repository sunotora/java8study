package jp.satomaru.java8study.util.launcher.message;

import java.util.Optional;
import java.util.function.Consumer;

import jp.satomaru.java8study.util.Closeables;
import jp.satomaru.java8study.util.launcher.validate.InvalidArgument;
import jp.satomaru.java8study.util.launcher.validate.InvalidParameter;
import lombok.RequiredArgsConstructor;

/**
 * モデルへの要求、およびモデルからの応答をサポートします。
 */
@RequiredArgsConstructor
public class Message {

	/** モデルへの要求。 */
	private final Request request;

	/** モデルからの応答。 */
	private final Response response;

	/**
	 * モデルへの要求から、インデックスに関連付けられた引数を取得します。
	 * 
	 * @param index 引数のインデックス
	 * @param type 引数の型
	 * @return 引数
	 */
	public <T> Optional<T> get(int index, Class<T> type) {
		return request.get(index, type);
	}

	/**
	 * モデルへの要求から、名前に関連付けられた引数を取得します。
	 * 
	 * @param name 引数の名前
	 * @param type 引数の型
	 * @return 引数
	 */
	public <T> Optional<T> get(String name, Class<T> type) {
		return request.get(name, type);
	}

	/**
	 * モデルからの応答を行います。
	 * 
	 * <p>
	 * 応答を行った後は、レスポンスを自動でクローズします。
	 * </p>
	 * 
	 * @param action レスポンスを受け取って、応答を行う関数
	 */
	public void response(Consumer<Response> action) {
		Closeables.autoCloseSecretly(response, action);
	}

	/**
	 * レスポンスに値を出力した後、レスポンスを自動でクローズします。
	 * 
	 * @param content 出力する値
	 */
	public void outputAndClose(Object content) {
		Closeables.autoCloseSecretly(response, r -> r.output(content));
	}

	/**
	 * レスポンスにエラーを出力した後、レスポンスを自動でクローズします。
	 * 
	 * @param invalid 引数例外
	 */
	public void errorAndClose(InvalidArgument invalid) {
		Closeables.autoCloseSecretly(response, r -> r.error(invalid));
	}

	/**
	 * レスポンスにエラーを出力した後、レスポンスを自動でクローズします。
	 * 
	 * @param invalid 名前つき引数例外
	 */
	public void errorAndClose(InvalidParameter invalid) {
		Closeables.autoCloseSecretly(response, r -> r.error(invalid));
	}
}
