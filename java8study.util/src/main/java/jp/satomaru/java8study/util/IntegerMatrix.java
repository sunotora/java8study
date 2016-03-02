package jp.satomaru.java8study.util;

import java.util.function.BiFunction;

/**
 * 2次元座標を割り振られた、整数の2次元配列です。
 */
public class IntegerMatrix extends Matrix<Integer> {

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 生成される要素の2次元座標は、
	 * X座標は0から幅-1、Y座標は0から高さ-1となります。
	 * 第3引数は、配列要素の初期値生成に使用します。
	 * </p>
	 * 
	 * @param width 幅（X座標の範囲）
	 * @param height 高さ（Y座標の範囲）
	 * @param positionToValue 2次元座標を受け取り、値を返す関数
	 */
	public IntegerMatrix(int width, int height, BiFunction<Integer, Integer, Integer> positionToValue) {
		super(width, height, positionToValue);
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param width 幅（X座標の範囲）
	 * @param height 高さ（Y座標の範囲）
	 * @param value 配列要素の値
	 */
	public IntegerMatrix(int width, int height, Integer value) {
		super(width, height, (x, y) -> value);
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
	 * @see #Matrix(int, int, BiFunction)
	 */
	public IntegerMatrix(int width, int height) {
		super(width, height);
	}

	/**
	 * 配列要素の値に、指定された値を加算します。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @param value 加算する値
	 * @return 配列要素
	 */
	public Item<Integer> add(int x, int y, int value) {
		Item<Integer> item = get(x, y);
		item.setValue(item.getValue() + value);
		return item;
	}

	/**
	 * 配列要素の値に、指定された値を加算します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @param value 加算する値
	 * @return 配列要素
	 */
	public Item<Integer> add(TwoDimensional twoDimensional, int value) {
		return add(twoDimensional.getX(), twoDimensional.getY(), value);
	}

	/**
	 * 配列要素の値を1増加します。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @return 配列要素
	 */
	public Item<Integer> increment(int x, int y) {
		return add(x, y, 1);
	}

	/**
	 * 配列要素の値を1増加します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 配列要素
	 */
	public Item<Integer> increment(TwoDimensional twoDimensional) {
		return increment(twoDimensional.getX(), twoDimensional.getY());
	}

	/**
	 * 配列要素の値を1減少します。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @return 配列要素
	 */
	public Item<Integer> decrement(int x, int y) {
		return add(x, y, -1);
	}

	/**
	 * 配列要素の値を1減少します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 配列要素
	 */
	public Item<Integer> decrement(TwoDimensional twoDimensional) {
		return decrement(twoDimensional.getX(), twoDimensional.getY());
	}
}
