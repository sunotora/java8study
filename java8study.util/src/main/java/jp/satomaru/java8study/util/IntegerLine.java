package jp.satomaru.java8study.util;

import java.util.function.IntFunction;

import jp.satomaru.java8study.util.variable.IndexedInteger;

/**
 * インデックス番号を割り振られた、整数の1次元配列です。
 */
public class IntegerLine extends LineBase<Integer, IndexedInteger> {

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 生成される要素のインデックス番号は、0から配列要素数-1となります。
	 * 第2引数は、配列要素の初期値生成に使用されます。
	 * </p>
	 * 
	 * @param size 配列要素数
	 * @param indexToValue インデックス番号を受け取り、値を返す関数
	 */
	public IntegerLine(int size, IntFunction<Integer> indexToValue) {
		super(size, index -> new IndexedInteger(index, indexToValue));
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param size 配列要素数
	 * @param value 配列要素の値
	 */
	public IntegerLine(int size, Integer value) {
		this(size, index -> value);
	}

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 配列要素の値はnullになります。
	 * </p>
	 * 
	 * @param size 配列要素数
	 */
	public IntegerLine(int size) {
		this(size, index -> null);
	}
}
