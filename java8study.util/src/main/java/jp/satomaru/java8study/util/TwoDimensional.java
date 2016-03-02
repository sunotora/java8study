package jp.satomaru.java8study.util;

/**
 * 2次元座標を持つことを表すインターフェースです。
 */
public interface TwoDimensional {

	/**
	 * X座標を取得します。
	 * 
	 * @return X座標
	 */
	int getX();

	/**
	 * Y座標を取得します。
	 * 
	 * @return Y座標
	 */
	int getY();

	/**
	 * 座標が等しいことを判定します。
	 * 
	 * @param other 比較する対象
	 * @return 座標が等しい場合はtrue
	 */
	default boolean isSamePosition(TwoDimensional other) {
		return getX() == other.getX() && getY() == other.getX();
	}

	/**
	 * 座標が異なることを判定します。
	 * 
	 * @param other 比較する対象
	 * @return 座標が異なる場合はtrue
	 */
	default boolean isNotSamePosition(TwoDimensional other) {
		return !isSamePosition(other);
	}
}
