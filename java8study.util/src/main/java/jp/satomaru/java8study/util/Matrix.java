package jp.satomaru.java8study.util;

import java.util.function.BiFunction;

import jp.satomaru.java8study.util.variable.TwoDimensionalVariable;
import lombok.ToString;

/**
 * 2次元座標を割り振られた2次元配列です。
 *
 * @param <T> 配列要素の値
 */
@ToString
public class Matrix<T> extends MatrixBase<T, TwoDimensionalVariable<T>> {

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 生成される要素の2次元座標は、
	 * X座標が0から幅-1、Y座標が0から高さ-1となります。
	 * 第3引数は、配列要素の初期値生成に使用されます。
	 * </p>
	 * 
	 * @param width 幅（X座標の範囲）
	 * @param height 高さ（Y座標の範囲）
	 * @param positionToValue X座標およびY座標を受け取り、値を返す関数
	 */
	public Matrix(int width, int height, BiFunction<Integer, Integer, T> positionToValue) {
		super(width, height, (x, y) -> new TwoDimensionalVariable<T>(x, y, positionToValue));
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param width 幅（X座標の範囲）
	 * @param height 高さ（Y座標の範囲）
	 * @param value 配列要素の値
	 */
	public Matrix(int width, int height, T value) {
		this(width, height, (x, y) -> value);
	}

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 配列要素の値はnullになります。
	 * </p>
	 * 
	 * @param width 幅（X座標の範囲）
	 * @param height 高さ（Y座標の範囲）
	 */
	public Matrix(int width, int height) {
		this(width, height, (x, y) -> null);
	}
}
