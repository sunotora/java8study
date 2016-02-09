package jp.satomaru.java8study;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
		// targetを延々と連ねるStream<Character>を生成
		// 個数をcountに制限する
		// 文字列に変換してStream<String>を生成
		// 全要素をくっつける
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

	public static Map<String, String> createMap(List<String> keyValueList) {
		// "key:value"という書式の文字列で構成された配列からMapを作成しましょう。ただしnullはスキップします。
//		Map<String, String> tempMap = new HashMap<>();
//
//		これだとnullが入ってきた時に落ちる
//		keyValueList.forEach(x -> {
//			String[] strs = x.split(":");
//			map.put(strs[0], strs[1]);
//		});
//
//		keyValueList.stream()
//			.filter(x -> x != null)
//			.forEach(x -> {String[] strs = x.split(":");
//						tempMap.put(strs[0], strs[1]);}
//			);
//
//		return tempMap;

		return keyValueList.stream()                                            // ListからStreamを生成。
				.filter(Objects::nonNull)                                       // null以外の要素のみ対象とする。
				.map(string -> string.split(":", 2))                            // コロンで2分割して、配列に変換する。
				.filter(pair -> pair.length == 2)                               // 2分割できたもののみ対象とする。
				.collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));   // 配列の1つ目をキー、2つ目を値として、Mapに集計する。
	}

	private Strings() {}
}
