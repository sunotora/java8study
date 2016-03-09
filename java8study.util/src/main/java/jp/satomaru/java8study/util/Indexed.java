package jp.satomaru.java8study.util;

import lombok.Value;

/**
 * インデックス番号を持つことを表すインターフェースです。
 */
public interface Indexed {

	/**
	 * インデックス番号の実装です。
	 */
	@Value
	public static final class Point implements Indexed {
		private final int index;
	}

	/**
	 * インデックス番号を取得します。
	 * 
	 * @return インデックス番号
	 */
	int getIndex();

	/**
	 * インデックスが等しいことを判定します。
	 * 
	 * @param other 比較する対象
	 * @return インデックスが等しい場合はtrue
	 */
	default boolean isSameIndex(Indexed other) {
		return getIndex() == other.getIndex();
	}

	/**
	 * インデックスが異なることを判定します。
	 * 
	 * @param other 比較する対象
	 * @return インデックスが異なる場合はtrue
	 */
	default boolean isNotSameIndex(Indexed other) {
		return !isSameIndex(other);
	}
}
