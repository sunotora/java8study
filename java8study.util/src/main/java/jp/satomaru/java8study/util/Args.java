package jp.satomaru.java8study.util;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

/**
 * 比較可能な引数の検査をサポートします。
 * 
 * @param <T> 引数の型
 */
@RequiredArgsConstructor(staticName = "of")
public final class Args<T extends Comparable<T>> {

	/** 引数名。 */
	private final String name;

	/** 引数の値。 */
	private final T value;

	/**
	 * 引数の値を取得します。
	 * 
	 * @return 引数の値
	 */
	public T get() {
		return value;
	}

	/**
	 * 値がnullでないことを検査します。
	 * 
	 * @return このオブジェクト自身
	 * @throws NullPointerException 値がnullである場合
	 */
	public Args<T> notNull() {
		if (value == null) {
			throw new NullPointerException(name);
		}

		return this;
	}

	/**
	 * 値が妥当でないことを検査します。
	 * 
	 * @param ng 妥当でない場合はtrueを返す関数
	 * @param messageSupplier 引数名と値を受け取り、例外メッセージを返す関数
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException 値が妥当でない場合
	 */
	public Args<T> illegal(Predicate<T> ng, BiFunction<String, T, String> messageSupplier) {
		if (value != null) {
			if (ng.test(value)) {
				new IllegalArgumentException(messageSupplier.apply(name, value));
			}
		}

		return this;
	}

	/**
	 * 値が妥当であることを検査します。
	 * 
	 * @param ok 妥当である場合はtrueを返す関数
	 * @param messageSupplier 引数名と値を受け取り、例外メッセージを返す関数
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException 値が妥当でない場合
	 */
	public Args<T> legal(Predicate<T> ok, BiFunction<String, T, String> messageSupplier) {
		return illegal(ok.negate(), messageSupplier);
	}

	/**
	 * 値が、とりうる値の内のいずれかであることを検査します。
	 * 
	 * @param first とりうる値（1つ目）
	 * @param others とりうる値（2つ目以降）
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException とりうる値の内のいずれでもない場合
	 */
	public Args<T> in(T first, @SuppressWarnings("unchecked") T... others) {
		return legal(
				value -> value.equals(first) || Stream.of(others).anyMatch(other -> value.equals(other)),
				(name, value) -> String.format("%s cannot appoint %s", name, value));
	}

	/**
	 * 値が、 禁止された値の内のいずれでもないことを検査します。
	 * 
	 * @param first 禁止された値（1つ目）
	 * @param others 禁止された値（2つ目以降）
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException 禁止された値の内のいずれかである場合
	 */
	public Args<T> not(T first, @SuppressWarnings("unchecked") T... others) {
		return illegal(
				value -> value.equals(first) || Stream.of(others).anyMatch(other -> value.equals(other)),
				(name, value) -> String.format("%s cannot appoint %s", name, value));
	}

	/**
	 * 値が最小値以上であることを検査します。
	 * 
	 * @param min 最小値
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException 最小値より小さい場合
	 */
	public Args<T> min(T min) {
		return legal(
				value -> value.compareTo(min) >= 0,
				(name, value) -> String.format("%s is greater than or equal to %s", name, min));
	}

	/**
	 * 値が最大値以下であることを検査します。
	 * 
	 * @param max 最大値
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException 最大値より大きい場合
	 */
	public Args<T> max(T max) {
		return legal(
				value -> value.compareTo(max) <= 0,
				(name, value) -> String.format("%s is less than or equal to %s", name, max));
	}

	/**
	 * 値が範囲内であることを検査します。
	 * 
	 * @param from 範囲の下限
	 * @param to 範囲の上限
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException 範囲内でない場合
	 */
	public Args<T> range(T from, T to) {
		return legal(
				value -> value.compareTo(from) >= 0 && value.compareTo(to) <= 0,
				(name, value) -> String.format("%s is between %s and %s", name, from, to));
	}

	/**
	 * 値が、禁止された範囲に入っていないことを検査します。
	 * 
	 * @param from 禁止された範囲の下限
	 * @param to 禁止された範囲の上限
	 * @return このオブジェクト自身
	 * @throws IllegalArgumentException 禁止された範囲に入っている場合
	 */
	public Args<T> exclude(T from, T to) {
		return illegal(
				value -> value.compareTo(from) >= 0 && value.compareTo(to) <= 0,
				(name, value) -> String.format("%s is other than %s to %s", name, from, to));
	}
}
