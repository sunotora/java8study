package jp.satomaru.java8study.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 2次元座標を割り振られた2次元配列です。
 *
 * @param <T> 配列要素の値
 */
@ToString
public class Matrix<T> {

	/** 2次元座標を割り振られた2次元配列要素です。 */
	@RequiredArgsConstructor
	@EqualsAndHashCode(of = {"x", "y"}, callSuper = false)
	public static final class Item<T> extends Variable<T, Item<T>> implements TwoDimensional {

		/** X座標。 */
		@Getter
		private final int x;

		/** Y座標。 */
		@Getter
		private final int y;

		/**
		 * 値を設定します。
		 * 
		 * @param positionToValue 2次元座標を受け取り、値を返す関数
		 * @return このオブジェクト自身
		 */
		public Item<T> setValue(BiFunction<Integer, Integer, T> positionToValue) {
			setValue(positionToValue.apply(x, y));
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
	private final List<List<Item<T>>> rows;

	/** 幅。 */
	private final int width;

	/** 高さ。 */
	private final int height;

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
		this.width = Args.of("width", width).min(1).get();
		this.height = Args.of("height", height).min(1).get();

		List<List<Item<T>>> rows = new ArrayList<>(height);

		for (int y = 0; y < height; y++) {
			List<Item<T>> row = new ArrayList<>(width);

			for (int x = 0; x < width; x++) {
				row.add(new Item<T>(x, y).setValue(positionToValue));
			}

			rows.add(Collections.unmodifiableList(row));
		}

		this.rows = Collections.unmodifiableList(rows);
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param width 幅（X座標の範囲）
	 * @param height 高さ（Y座標の範囲）
	 * @param value 配列要素の値
	 * @see #Matrix(int, int, BiFunction)
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
		return rows.get(assertY(y)).get(assertX(x));
	}

	/**
	 * 指定されたオブジェクトと2次元座標が等しい配列要素を取得します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 配列要素
	 */
	public Item<T> get(TwoDimensional twoDimensional) {
		return get(twoDimensional.getX(), twoDimensional.getY());
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
		return rows.get(assertY(y)).stream();
	}

	/**
	 * 指定されたX座標の配列要素のみを含んだストリームを取得します。
	 * 
	 * @param x X座標
	 * @return ストリーム
	 */
	public Stream<Item<T>> col(int x) {
		assertX(x);
		return rows.stream().map(row -> row.get(x));
	}

	/**
	 * 幅を取得します。
	 * 
	 * @return 幅
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 高さを取得します。
	 * 
	 * @return 高さ
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 配列要素数を取得します。
	 * 
	 * @return 配列要素数
	 */
	public int size() {
		return width * height;
	}

	/**
	 * X座標が妥当であることを検査します。
	 * 
	 * @param x X座標
	 * @return X座標
	 */
	private int assertX(int x) {
		return Args.of("x", x).range(0, width - 1).get();
	}

	/**
	 * Y座標が妥当であることを検査します。
	 * 
	 * @param y Y座標
	 * @return Y座標
	 */
	private int assertY(int y) {
		return Args.of("y", y).range(0, height - 1).get();
	}
}
