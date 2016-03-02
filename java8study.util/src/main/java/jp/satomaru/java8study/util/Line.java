package jp.satomaru.java8study.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * インデックス番号を割り振られた1次元配列です。
 *
 * @param <T> 配列要素の値
 */
public class Line<T> {

	/** インデックス番号を割り振られた1次元配列要素です。 */
	@RequiredArgsConstructor
	@Getter
	@EqualsAndHashCode(of = {"index"})
	public static final class Item<T> implements Indexed {

		/** インデックス番号。 */
		private final int index;

		/**　値。 */
		private T value;

		/**
		 * 値を設定します。
		 * 
		 * @param value 値
		 * @return このオブジェクト自身
		 */
		public Item<T> setValue(T value) {
			this.value = value;
			return this;
		}

		/**
		 * 値を設定します。
		 * 
		 * @param indexToValue インデックス番号を受け取り、値を返す関数
		 * @return このオブジェクト自身
		 */
		public Item<T> setValue(Function<Integer, T> indexToValue) {
			value = indexToValue.apply(index);
			return this;
		}

		/**
		 * 値を判定します。
		 * 
		 * @param valueTester 値を受け取り、booleanを返す関数
		 * @return 引数の実行結果
		 */
		public boolean test(Predicate<T> valueTester) {
			return valueTester.test(value);
		}

		/**
		 * この要素を処理します。
		 * 
		 * @param action インデックス番号と値を受け取る関数
		 */
		public void accept(BiConsumer<Integer, T> action) {
			action.accept(index, value);
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
	 * 配列要素の値を取得します。
	 * 
	 * @param index インデックス番号
	 * @return 配列要素の値
	 */
	public T getValue(int index) {
		return get(index).getValue();
	}

	/**
	 * 指定されたオブジェクトとインデックス番号が等しい配列要素の値を取得します。
	 * 
	 * @param indexed インデックス番号を持つオブジェクト
	 * @return 配列要素の値
	 */
	public T getValue(Indexed indexed) {
		return getValue(indexed.getIndex());
	}

	/**
	 * 配列要素の値を設定します。
	 * 
	 * @param index インデックス番号
	 * @param value 配列要素の値
	 */
	public void setValue(int index, T value) {
		get(index).setValue(value);
	}

	/**
	 * 指定されたオブジェクトとインデックス番号が等しい配列要素の値を設定します。
	 * 
	 * @param indexed インデックス番号を持つオブジェクト
	 * @param value 配列要素の値
	 */
	public void setValue(Indexed indexed, T value) {
		setValue(indexed.getIndex(), value);
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

	/**
	 * 文字列表現を取得します。
	 * 
	 * @return 文字列表現
	 */
	@Override
	public String toString() {
		return elements.toString();
	}
}
