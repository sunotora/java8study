package jp.satomaru.java8study.util;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 数に関するユーティリティです。
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Numbers {

	/**
	 * 正の整数の乱数を発生させます。
	 * 
	 * <p>
	 * 発生する乱数は、0から引数の最大値-1までの整数です。
	 * </p>
	 * 
	 * @param maxExclusive 最大値（ただしこの数を含まない）
	 * @return 0から引数の最大値-1までのいずれかの数
	 */
	public static int randomInt(int maxExclusive) {
		return (int) Math.floor(Math.random() * Args.of("maxExclusive", maxExclusive).min(0).get());
	}

	/**
	 * 0であることを判定します。
	 * 
	 * @param decimal 判定対象
	 * @return 0である場合はtrue
	 */
	public static boolean isZero(BigDecimal decimal) {
		return decimal.compareTo(BigDecimal.ZERO) == 0;
	}
}
