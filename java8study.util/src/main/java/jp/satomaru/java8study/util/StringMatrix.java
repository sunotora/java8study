package jp.satomaru.java8study.util;

import java.util.function.BinaryOperator;

import jp.satomaru.java8study.util.variable.TwoDimensionalString;

/**
 * 2次元座標を割り振られた、文字列の2次元配列です。
 */
public class StringMatrix extends MatrixBase<String, TwoDimensionalString> {

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
	public StringMatrix(int width, int height, BinaryOperator<String> positionToValue) {
		super(width, height, (x, y) -> new TwoDimensionalString(x, y, positionToValue));
	}

	/**
	 * コンストラクタ。
	 *
	 * @param width 幅（X座標の範囲）
	 * @param height 高さ（Y座標の範囲）
	 * @param value 配列要素の値
	 */
	public StringMatrix(int width, int height, String value) {
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
	public StringMatrix(int width, int height) {
		this(width, height, (x, y) -> null);
	}
}
