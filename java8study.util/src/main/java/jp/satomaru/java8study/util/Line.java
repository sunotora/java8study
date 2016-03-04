package jp.satomaru.java8study.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * インデックス番号を割り振られた1次元配列です。
 *
 * @param <T> 配列要素の値
 */
@ToString
public class Line<T> {

	/** インデックス番号を割り振られた1次元配列要素です。 */
	@RequiredArgsConstructor
	@EqualsAndHashCode(of = {"index"}, callSuper = false)
	public static final class Item<T> extends Variable<T, Item<T>> implements Indexed {

		/** インデックス番号。 */
		@Getter
		private final int index;

		/**
		 * 値を設定します。
		 * 
		 * @param indexToValue インデックス番号を受け取り、値を返す関数
		 * @return このオブジェクト自身
		 */
		public Item<T> setValue(Function<Integer, T> indexToValue) {
			setValue(indexToValue.apply(index));
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Item<T> self() {
			return this;
		}
	}

	/** 配列要素。 */
	private final List<Item<T>> elements;

	/** 配列要素数。 */
	private final int size;

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
	public Line(int size, Function<Integer, T> indexToValue) {
		this.size = Args.of("size", size).min(1).get();

		List<Item<T>> elements = new ArrayList<>(size);

		for (int i = 0; i < size; i++) {
			elements.add(new Item<T>(i).setValue(indexToValue));
		}

		this.elements = Collections.unmodifiableList(elements);
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param size 配列要素数
	 * @param value 配列要素の値
	 * @see #Line(int, Function)
	 */
	public Line(int size, T value) {
		this(size, i -> value);
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
	public Line(int size) {
		this(size, i -> null);
	}

	/**
	 * 配列要素を取得します。
	 * 
	 * @param index インデックス番号
	 * @return 配列要素
	 */
	public Item<T> get(int index) {
		return elements.get(Args.of("index", index).range(0, size - 1).get());
	}

	/**
	 * 指定されたオブジェクトとインデックス番号が等しい配列要素を取得します。
	 * 
	 * @param indexed インデックス番号を持つオブジェクト
	 * @return 配列要素
	 */
	public Item<T> get(Indexed indexed) {
		return get(indexed.getIndex());
	}

	/**
	 * 全ての配列要素を含んだストリームを取得します。
	 * 
	 * @return ストリーム
	 */
	public Stream<Item<T>> stream() {
		return elements.stream();
	}

	/**
	 * 配列要素数を取得します。
	 * 
	 * @return 配列要素数
	 */
	public int size() {
		return size;
	}
}
