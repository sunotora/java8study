package jp.satomaru.java8study.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * 数に関するユーティリティです。
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Numbers {

	/**
	 * 整数の乱数を発生させます。
	 * 
	 * <p>
	 * 発生する乱数は、0から引数-1までの整数です。
	 * </p>
	 * 
	 * @param maxExclusive 最大値（ただしこの数を含まない）
	 * @return 0からmaxExclusive-1までのいずれかの数
	 */
	public static int randomInt(int maxExclusive) {
		return (int) Math.floor(Math.random() * maxExclusive);
	}
}
