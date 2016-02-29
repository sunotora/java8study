package jp.satomaru.java8study.util;

import java.util.Objects;

/**
 * 3つの引数を受け取り、結果を返さない関数です。
 *
 * @param <T> 第1引数
 * @param <U> 第2引数
 * @param <V> 第3引数
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

	/**
	 * 処理を行います。
	 * 
	 * @param t 第1引数
	 * @param u 第2引数
	 * @param v 第3引数
	 */
	void accept(T t, U u, V v);

	/**
	 * この関数を実行した後に引数の関数を実行するように、この関数と引数の関数を合成します。
	 * 
	 * @param after この関数の後に実行する関数
	 * @return 合成された関数
	 */
	default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
		Objects.requireNonNull(after);

		return (a, b, c) -> {
			accept(a, b, c);
			after.accept(a, b, c);
		};
	}
}
