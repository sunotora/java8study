package jp.satomaru.java8study.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * 3つの引数を受け取り、結果を返す関数です。
 *
 * @param <T> 第1引数
 * @param <U> 第2引数
 * @param <V> 第3引数
 * @param <R> 結果
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

	/**
	 * 処理を行います。
	 * 
	 * @param t 第1引数
	 * @param u 第2引数
	 * @param v 第3引数
	 * @return 結果
	 */
	R apply(T t, U u, V v);

	/**
	 * この関数を実行した後に引数の関数を実行するように、この関数と引数の関数を合成します。
	 * 
	 * @param <W> 合成された関数の結果
	 * @param after この関数の後に実行する関数
	 * @return 合成された関数
	 */
	default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
		Objects.requireNonNull(after);
		return (T t, U u, V v) -> after.apply(apply(t, u, v));
	}
}
