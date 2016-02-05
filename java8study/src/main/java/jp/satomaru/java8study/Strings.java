package jp.satomaru.java8study;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文字列ユーティリティ。
 */
public final class Strings {

	/**
	 * 数値に変換します。
	 * 
	 * @param string 数値を表す文字列
	 * @return 数値（文字列がnullの場合は0）
	 */
	public static BigDecimal toBigDecimal(String string) {
		return Optional.ofNullable(string)
				.map(BigDecimal::new)
				.orElse(BigDecimal.ZERO);
	}

	/**
	 * 文字を繰り返します。
	 * 
	 * @param target 対象の文字
	 * @param number 繰り返し回数
	 * @return 文字列
	 */
	public static String repeat(char target, int count) {
		return Stream.generate(() -> target)
				.limit(count)
				.map(String::valueOf)
				.collect(Collectors.joining());
	}

	// TODO 文字数を数えるメソッドを実装しましょう。ただし、nullの時は0文字とします。

	// TODO "key:value"という書式の文字列で構成された配列からMapを作成しましょう。ただしnullはスキップします。

	private Strings() {}
}
