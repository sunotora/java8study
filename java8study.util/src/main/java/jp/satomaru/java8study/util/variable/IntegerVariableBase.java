package jp.satomaru.java8study.util.variable;

import java.util.NoSuchElementException;

/**
 * 整数を保持する変数の基底クラスです。
 *
 * @param <S> サブクラス
 */
public abstract class IntegerVariableBase<S extends IntegerVariableBase<S>> extends VariableBase<Integer, S> {

	/**
	 * 加算します。
	 * 
	 * @param arg 加算する値
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S add(int arg) {
		return setValue(value -> value + arg);
	}

	/**
	 * 減算します。
	 * 
	 * @param arg 減算する値
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S subtract(int arg) {
		return setValue(value -> value - arg);
	}

	/**
	 * 乗算します。
	 * 
	 * @param arg 乗算する値
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S multiply(int arg) {
		return setValue(value -> value * arg);
	}

	/**
	 * 除算します。
	 * 
	 * @param arg 除算する値
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S divide(int arg) {
		return setValue(value -> value / arg);
	}

	/**
	 * 除算して、その剰余を設定します。
	 * 
	 * @param arg 除算する値
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S remainder(int arg) {
		return setValue(value -> value % arg);
	}

	/**
	 * べき乗します。
	 * 
	 * @param arg べき乗する値
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S power(int arg) {
		return setValue(value -> (int) Math.pow(value, arg));
	}

	/**
	 * 1増加します。
	 * 
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S increment() {
		return add(1);
	}

	/**
	 * 1減少します。
	 * 
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S decrement() {
		return subtract(1);
	}
}
