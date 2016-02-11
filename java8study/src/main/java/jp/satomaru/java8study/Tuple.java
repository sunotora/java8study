package jp.satomaru.java8study;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * ２つの値を保持します。
 * 
 * @param <F> 一つ目の値
 * @param <S> 二つ目の値
 */
@RequiredArgsConstructor(staticName="of")
@Value
public final class Tuple<F, S> {

	/**
	 * 配列からタプルを生成します。
	 * 
	 * @param array 配列
	 * @return タプル
	 */
	public static <T> Tuple<T, T> of(T[] array) {
		switch (array.length) {
			case 0: return Tuple.of(null, null);
			case 1: return Tuple.of(array[0], null);
			default: return Tuple.of(array[0], array[1]);
		}
	}

	/**
	 * マップエントリーからタプルを生成します。
	 * 
	 * @param entry マップエントリー
	 * @return タプル
	 */
	public static <F, S> Tuple<F, S> of(Map.Entry<F, S> entry) {
		return Tuple.of(entry.getKey(), entry.getValue());
	}

	/** 一つ目の値。 */
	private final F first;

	/** 二つ目の値。 */
	private final S second;

	/**
	 * 新しいタプルを生成します。
	 * 
	 * @param forFirst 一つ目の値を変換する関数
	 * @param forSecond 二つ目の値を変換する関数
	 * @return 新しいタプル
	 */
	public <T, U> Tuple<T, U> map(Function<F, T> forFirst, Function<S, U> forSecond) {
		return Tuple.of(forFirst.apply(first), forSecond.apply(second));
	}

	/**
	 * 一つ目の値のみ変換して、新しいタプルを生成します。
	 * 
	 * @param forFirst 一つ目の値を変換する関数
	 * @return 新しいタプル
	 */
	public <T> Tuple<T, S> map1st(Function<F, T> forFirst) {
		return Tuple.of(forFirst.apply(first), second);
	}

	/**
	 * 二つ目の値のみ変換して、新しいタプルを生成します。
	 * 
	 * @param forSecond 二つ目の値を変換する関数
	 * @return 新しいタプル
	 */
	public <T> Tuple<F, T> map2nd(Function<S, T> forSecond) {
		return Tuple.of(first, forSecond.apply(second));
	}

	/**
	 * 処理を行います。
	 * 
	 * @param action このタプルを処理する関数
	 * @return 処理結果
	 */
	public <T> T apply(BiFunction<F, S, T> action) {
		return action.apply(first, second);
	}

	/**
	 * 文字列に編集します。
	 * 
	 * @param format 編集形式
	 * @return 編集された文字列
	 */
	public String format(String format) {
		return String.format(format, first, second);
	}
}
