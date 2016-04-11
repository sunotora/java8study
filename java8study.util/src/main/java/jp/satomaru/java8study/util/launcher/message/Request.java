package jp.satomaru.java8study.util.launcher.message;

import java.util.Optional;

/**
 * 実行するオブジェクトへの要求です。
 */
public interface Request {

	/**
	 * コマンドを取得します。
	 * 
	 * @return コマンド
	 */
	Optional<String> getCommand();

	/**
	 * インデックスに関連付けられた引数を取得します。
	 * 
	 * @param <T> 引数の型
	 * @param index 引数のインデックス
	 * @param type 引数の型
	 * @return 引数
	 */
	<T> Optional<T> get(int index, Class<T> type);

	/**
	 * 名前に関連付けられた引数を取得します。
	 * 
	 * @param <T> 引数の型
	 * @param name 引数の名前
	 * @param type 引数の型
	 * @return 引数
	 */
	<T> Optional<T> get(String name, Class<T> type);

	/**
	 * インデックスに関連付けられた引数の個数を取得します。
	 * 
	 * @return インデックスに関連付けられた引数の個数
	 */
	int getArgumentsSize();

	/**
	 * 名前に関連付けられた引数が存在することを判定します。
	 * 
	 * @param name 引数の名前
	 * @return 存在する場合はtrue（値がemptyの場合も含む）
	 */
	boolean isParameterExisted(String name);
}
