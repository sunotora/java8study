package jp.satomaru.java8study.util;

import java.util.function.IntFunction;

import jp.satomaru.java8study.util.variable.IndexedVariable;
import lombok.ToString;

/**
 * インデックス番号を割り振られた1次元配列です。
 *
 * @param <T> 配列要素の値
 */
@ToString
public class Line<T> extends LineBase<T, IndexedVariable<T>> {

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
	public Line(int size, IntFunction<T> indexToValue) {
		super(size, index -> new IndexedVariable<T>(index, indexToValue));
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param size 配列要素数
	 * @param value 配列要素の値
	 */
	public Line(int size, T value) {
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
	public Line(int size) {
		this(size, index -> null);
	}
}
