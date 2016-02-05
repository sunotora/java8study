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
		// nullを許可するOptional<String>を作る
		// そのOptionalに対してnew BigDecimal(x)を適用 → 型はOptional<BigDecimal>となる
		// Optionalの中身がnullでなければそれを、nullの場合は0を返却する
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
		// targetのストリームを生成
		// ストリームからcount分取ってくる
		// 文字列に変換
		// 全文字列をくっつけて返却
		return Stream.generate(() -> target)
				.limit(count)
				.map(String::valueOf)
				.collect(Collectors.joining());
	}

	public static int count(String string) {
		// 文字数を数えるメソッドを実装しましょう。ただし、nullの時は0文字とします。
		return Optional.ofNullable(string)
				.map(String::length)
				.orElse(0);
	}

	// TODO "key:value"という書式の文字列で構成された配列からMapを作成しましょう。ただしnullはスキップします。

	private Strings() {}
}
