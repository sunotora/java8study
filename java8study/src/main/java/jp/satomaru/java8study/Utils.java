package jp.satomaru.java8study;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文字列ユーティリティ。
 */
public final class Utils {

	/**
	 * 数値に変換します。
	 *
	 * @param string 数値を表す文字列
	 * @return 数値（文字列がnullの場合は0）
	 */
	public static BigDecimal toBigDecimal(String string) {
		return Optional.ofNullable(string)  // nullを許可するOptional<String>を作る。
				.map(BigDecimal::new)       // そのOptionalに対してnew BigDecimal(x)を適用 → 型はOptional<BigDecimal>となる。
				.orElse(BigDecimal.ZERO);   // Optionalの中身がnullでなければそれを、nullの場合は0を返却する。

	}

	/**
	 * 文字を繰り返します。
	 *
	 * @param target 文字
	 * @param number 繰り返し回数
	 * @return 文字列
	 */
	public static String repeat(char target, int count) {
		return Stream.generate(() -> target)        // targetを延々と連ねるStream<Character>を生成する。
				.limit(count)                       // 個数をcountに制限する。
				.map(String::valueOf)               // 文字列に変換してStream<String>を生成する。
				.collect(Collectors.joining());     // 全要素を文字列連結する。
	}

	/**
	 * 文字数を数えます。
	 * 
	 * @param string 文字列
	 * @return 文字数（nullの場合は0）
	 */
	public static int count(String string) {
		return Optional.ofNullable(string)
				.map(String::length)
				.orElse(0);
	}

	/**
	 * "key:value"という書式の文字列リストから、マップを生成します。
	 * 
	 * @param list 文字列リスト
	 * @return マップ（書式が妥当でないものは含まない）
	 */
	public static Map<String, String> toMap(List<String> keyValueList) {
		return keyValueList.stream()                                            // ListからStreamを生成。
				.filter(Objects::nonNull)                                       // null以外の要素のみ対象とする。
				.map(string -> string.split(":", 2))                            // コロンで2分割して、配列に変換する。
				.filter(pair -> pair.length == 2)                               // 2分割できたもののみ対象とする。
				.collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));   // 配列の1つ目をキー、2つ目を値として、Mapに集計する。
	}

	/**
	 * マップから、"key:value"という書式の文字列リストを生成します。
	 * 
	 * @param map マップ
	 * @return 各エントリーを"key:value"に編集したリスト
	 */
	public static <K, V> List<String> toStringList(Map<K, V> map) {
		return map.entrySet().stream()
				.map(Tuple::of)
				.map(tuple -> tuple.map2nd(Utils::defaultString))
				.map(tuple -> tuple.format("%s:%s"))
				.collect(Collectors.toList());
	}

	/**
	 * コレクション内のオブジェクトを、型ごとに集めます。
	 * 
	 * @param collection コレクション
	 * @return 型ごとに集めた結果を格納したマップ
	 */
	public static Map<Class<?>, List<Object>> groupByType(Collection<?> collection) {
		// TODO 実装してください。
		return null;
	}

	/**
	 * 文字列に変換します。
	 * 
	 * @param object 対象
	 * @return 文字列（nullの場合はブランク文字列）
	 */
	public static String defaultString(Object object) {
		return (object != null) ? String.valueOf(object) : "";
	}

	private Utils() {}
}
