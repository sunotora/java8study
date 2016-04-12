package jp.satomaru.java8study.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import jp.satomaru.java8study.util.variable.VariableBase;
import lombok.ToString;

/**
 * 2次元座標を割り振られた2次元配列の基底クラスです。
 *
 * @param <T> 配列要素の値
 * @param <E> 配列要素
 */
@ToString
public abstract class MatrixBase<T, E extends VariableBase<T, E> & TwoDimensional> {

	/** 配列要素。 */
	private final Map<Integer, Map<Integer, E>> elements;

	/** 幅。 */
	private final int width;

	/** 高さ。 */
	private final int height;

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
	 * @param positionToValue X座標およびY座標を受け取り、配列要素を返す関数
	 * @throws IllegalArgumentException 幅または高さがマイナスである場合
	 */
	public MatrixBase(int width, int height, BiFunction<Integer, Integer, E> positionToValue) {
		this.width = Args.of("width", width).min(0).get();
		this.height = Args.of("height", height).min(0).get();

		Map<Integer, Map<Integer, E>> rows = new HashMap<>(height);

		for (int y = 0; y < height; y++) {
			Map<Integer, E> row = new HashMap<>(width);

			for (int x = 0; x < width; x++) {
				row.put(x, positionToValue.apply(x, y));
			}

			rows.put(y, Collections.unmodifiableMap(row));
		}

		this.elements =  Collections.unmodifiableMap(rows);
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
	 * 配列要素を取得します。
	 *
	 * @param x X座標
	 * @param y Y座標
	 * @return 配列要素
	 */
	public final Optional<E> optional(int x, int y) {
		return (elements.containsKey(y))
			? Optional.ofNullable(elements.get(y).get(x))
			: Optional.empty();
	}

	/**
	 * 指定されたオブジェクトと2次元座標が等しい配列要素を取得します。
	 *
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 配列要素
	 */
	public final Optional<E> optional(TwoDimensional twoDimensional) {
		return optional(twoDimensional.getX(), twoDimensional.getY());
	}

	/**
	 * 配列要素を取得します。
	 *
	 * @param x X座標
	 * @param y Y座標
	 * @return 配列要素
	 * @throws NoSuchElementException 配列要素が存在しない場合。
	 */
	public E get(int x, int y) {
		return optional(x, y).get();
	}

	/**
	 * 指定されたオブジェクトと2次元座標が等しい配列要素を取得します。
	 *
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 配列要素
	 * @throws NoSuchElementException 配列要素が存在しない場合。
	 */
	public E get(TwoDimensional twoDimensional) {
		return optional(twoDimensional).get();
	}

	/**
	 * 全ての配列要素を含んだストリームを取得します。
	 *
	 * @return ストリーム
	 */
	public Stream<E> flat() {
		return elements.values().stream().flatMap(row -> row.values().stream());
	}

	/**
	 * 全ての配列要素の値を含んだストリームを取得します。
	 *
	 * @return ストリーム
	 */
	public Stream<T> values() {
		return flat().map(VariableBase::getValue);
	}

	/**
	 * 指定されたY座標の配列要素のみを含んだストリームを取得します。
	 *
	 * @param y Y座標
	 * @return ストリーム
	 */
	public Stream<E> row(int y) {
		return elements.get(assertY(y)).values().stream();
	}

	/**
	 * 指定されたY座標の配列要素のみの値を含んだストリームを取得します。
	 *
	 * @param y Y座標
	 * @return ストリーム
	 */
	public Stream<T> rowValues(int y) {
		return row(y).map(VariableBase::getValue);
	}

	/**
	 * 指定されたX座標の配列要素のみを含んだストリームを取得します。
	 *
	 * @param x X座標
	 * @return ストリーム
	 */
	public Stream<E> col(int x) {
		assertX(x);
		return elements.values().stream().map(row -> row.get(x));
	}

	/**
	 * 指定されたX座標の配列要素のみの値を含んだストリームを取得します。
	 *
	 * @param x X座標
	 * @return ストリーム
	 */
	public Stream<T> colValues(int x) {
		return col(x).map(VariableBase::getValue);
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

	/**
	 * 指定されたオブジェクトの周囲の要素を含んだストリームを取得します。
	 * 例）  1,  2,  3,  4
	 *       5,  6,  7,  8
	 *       9, 10, 11, 12
	 *      13, 14, 15, 16
	 *      17, 18, 19, 20
	 *     入力が"7"の座標：返却ストリーム{2,3,4,6,8,10,11,12}
	 *     入力が"1"の座標：返却ストリーム{2,5,6}
	 *     入力が"20"の座標：返却ストリーム{15,16,19}
	 * @param criteriaDimension 2次元座標を持つオブジェクト（基準）
	 * @return 配列要素
	 */
	public final Stream<E> arround(TwoDimensional criteriaDimension) {
		return arround(criteriaDimension.getX(), criteriaDimension.getY());
	}

	/**
	 * 指定された座標の周囲の要素を含んだストリームを取得します。
	 * @param criteriaX 基準X座標
	 * @param criteriaY 基準Y座標
	 * @return 配列要素
	 */
	public final Stream<E> arround(int criteriaX, int criteriaY) {
		return flat().filter(elem -> isArround(elem.getX(), elem.getY(), criteriaX, criteriaY));
	}

	private final int MIN_ARROUND_RANGE = -1;
	private final int MAX_ARROUND_RANGE = 1;

	/**
	 * 指定されたオブジェクトの周囲かどうか判定します。
	 * @param x X座標
	 * @param y Y座標
	 * @param twoDimensional 指定された2次元座標を持つオブジェクト
	 * @return
	 */
	private final boolean isArround(int x, int y, TwoDimensional twoDimensional) {
		return isArround(x, y, twoDimensional.getX(), twoDimensional.getY());
	}

	/**
	 * 指定されたオブジェクトの周囲かどうか判定します。
	 * @param x X座標
	 * @param y Y座標
	 * @param criteriaX 指定されたオブジェクトのX座標
	 * @param criteriaY 指定されたオブジェクトのY座標
	 * @return
	 */
	private final boolean isArround(int x, int y, int criteriaX, int criteriaY) {
		// 座標が同じ場合はfalse;
		if (x == criteriaX && y == criteriaY) return false;

		int substractionX = criteriaX - x;
		int substractionY = criteriaY - y;
		return substractionX >= MIN_ARROUND_RANGE && substractionX <=MAX_ARROUND_RANGE
				&& substractionY >= MIN_ARROUND_RANGE && substractionY <=MAX_ARROUND_RANGE;
	}

}
