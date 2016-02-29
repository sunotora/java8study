package jp.satomaru.java8study.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 2次元座標を割り振られた2次元配列です。
 *
 * @param <T> 配列要素の値
 */
public class Matrix<T> {

	/** 2次元座標を割り振られた2次元配列要素です。 */
	@RequiredArgsConstructor
	@Getter
	@EqualsAndHashCode(of = {"x", "y"})
	public static final class Item<T> implements TwoDimensional {

		/** X座標。 */
		private final int x;

		/** Y座標。 */
		private final int y;

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
		 * @param positionToValue 2次元座標を受け取り、値を返す関数
		 * @return このオブジェクト自身
		 */
		public Item<T> setValue(BiFunction<Integer, Integer, T> positionToValue) {
			value = positionToValue.apply(x, y);
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
		 * @param action 2次元座標と値を受け取る関数
		 */
		public void accept(TriConsumer<Integer, Integer, T> action) {
			action.accept(x, y, value);
		}
	}

	/** 配列要素。 */
	private final List<List<Item<T>>> rows;

	/** 配列要素数。 */
	private final int size;

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
	public Matrix(int width, int height, BiFunction<Integer, Integer, T> positionToValue) {
		List<List<Item<T>>> rows = new ArrayList<>(height);

		for (int y = 0; y < height; y++) {
			List<Item<T>> row = new ArrayList<>(width);

			for (int x = 0; x < width; x++) {
				row.add(new Item<T>(x, y).setValue(positionToValue));
			}

			rows.add(Collections.unmodifiableList(row));
		}

		this.rows = Collections.unmodifiableList(rows);
		this.size = width * height;
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
	public Matrix(int width, int height) {
		this(width, height, (x, y) -> null);
	}

	/**
	 * 配列要素を取得します。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @return 配列要素
	 */
	public Item<T> get(int x, int y) {
		return rows.get(y).get(x);
	}

	/**
	 * 指定されたオブジェクトと2次元座標が等しい配列要素を取得します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 配列要素
	 */
	public Item<T> get(TwoDimensional twoDimensional) {
		return rows.get(twoDimensional.getY()).get(twoDimensional.getX());
	}

	/**
	 * 配列要素の値を取得します。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @return 配列要素の値
	 */
	public T getValue(int x, int y) {
		return rows.get(y).get(x).getValue();
	}

	/**
	 * 指定されたオブジェクトと2次元座標が等しい配列要素の値を取得します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 配列要素の値
	 */
	public T getValue(TwoDimensional twoDimensional) {
		return rows.get(twoDimensional.getY()).get(twoDimensional.getX()).getValue();
	}

	/**
	 * 配列要素の値を設定します。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @param value 配列要素の値
	 */
	public void setValue(int x, int y, T value) {
		rows.get(y).get(x).setValue(value);
	}

	/**
	 * 指定されたオブジェクトと2次元座標が等しい配列要素の値を設定します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @param value 配列要素の値
	 */
	public void setValue(TwoDimensional twoDimensional, T value) {
		rows.get(twoDimensional.getY()).get(twoDimensional.getX()).setValue(value);
	}

	/**
	 * 全ての配列要素を含んだストリームを取得します。
	 * 
	 * @return ストリーム
	 */
	public Stream<Item<T>> flat() {
		return rows.stream().flatMap(List::stream);
	}

	/**
	 * 指定されたY座標の配列要素のみを含んだストリームを取得します。
	 * 
	 * @param y Y座標
	 * @return ストリーム
	 */
	public Stream<Item<T>> row(int y) {
		return rows.get(y).stream();
	}

	/**
	 * 指定されたX座標の配列要素のみを含んだストリームを取得します。
	 * 
	 * @param x X座標
	 * @return ストリーム
	 */
	public Stream<Item<T>> col(int x) {
		return rows.stream().map(row -> row.get(x));
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
		return rows.toString();
	}
}
