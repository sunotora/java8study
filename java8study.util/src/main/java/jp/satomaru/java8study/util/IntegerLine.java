package jp.satomaru.java8study.util;

import java.util.function.Function;

/**
 * インデックス番号を割り振られた、整数の1次元配列です。
 */
public class IntegerLine extends Line<Integer> {

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 生成される要素のインデックス番号は、0から配列要素数-1となります。
	 * 第2引数は、配列要素の初期値生成に使用します。
	 * </p>
	 * 
	 * @param size 配列要素数
	 * @param indexToValue インデックス番号を受け取り、値を返す関数
	 */
	public IntegerLine(int size, Function<Integer, Integer> indexToValue) {
		super(size, indexToValue);
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param size 配列要素数
	 * @param value 配列要素の値
	 * @see #Line(int, Function)
	 */
	public IntegerLine(int size, Integer value) {
		super(size, index -> value);
	}

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 配列要素の値はnullになります。
	 * </p>
	 * 
	 * @param size 配列要素数
	 * @see #Line(int, Function)
	 */
	public IntegerLine(int size) {
		super(size);
	}

	/**
	 * 配列要素の値に、指定された値を加算します。
	 * 
	 * @param index インデックス番号
	 * @param value 加算する値
	 * @return 配列要素
	 */
	public Item<Integer> add(int index, int value) {
		Item<Integer> item = get(index);
		item.setValue(item.getValue() + value);
		return item;
	}

	/**
	 * 配列要素の値に、指定された値を加算します。
	 * 
	 * @param indexed インデックス番号を持つオブジェクト
	 * @param value 加算する値
	 * @return 配列要素
	 */
	public Item<Integer> add(Indexed indexed, int value) {
		return add(indexed.getIndex(), value);
	}

	/**
	 * 配列要素の値を1増加します。
	 * 
	 * @param index インデックス番号
	 * @return 配列要素
	 */
	public Item<Integer> increment(int index) {
		return add(index, 1);
	}

	/**
	 * 配列要素の値を1増加します。
	 * 
	 * @param indexed インデックス番号を持つオブジェクト
	 * @return 配列要素
	 */
	public Item<Integer> increment(Indexed indexed) {
		return increment(indexed.getIndex());
	}

	/**
	 * 配列要素の値を1減少します。
	 * 
	 * @param index インデックス番号
	 * @return 配列要素
	 */
	public Item<Integer> decrement(int index) {
		return add(index, -1);
	}

	/**
	 * 配列要素の値を1減少します。
	 * 
	 * @param indexed インデックス番号を持つオブジェクト
	 * @return 配列要素
	 */
	public Item<Integer> decrement(Indexed indexed) {
		return decrement(indexed.getIndex());
	}
}
