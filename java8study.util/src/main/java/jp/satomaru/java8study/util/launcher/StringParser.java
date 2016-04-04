package jp.satomaru.java8study.util.launcher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 簡易的な文字列パーサーです。
 */
public final class StringParser {

	/** 唯一のインスタンス。 */
	private static final StringParser INSTANCE = new StringParser();

	/** 日付パターン。 */
	private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4}|\\d{2})[./-](\\d{1,2})[./-](\\d{1,2})");

	/** 時刻パターン。 */
	private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})(\\.(\\d{1,9}))?");

	/**
	 * 文字列を解析します。
	 * 
	 * <p>
	 * 対応する型は以下の通りです。
	 * </p>
	 * 
	 * <ul>
	 * <li>String<ul><li>実際には何も行いません。</li></ul></li>
	 * <li>Boolean<ul><li>前後の空白は無視されます。</li></ul></li>
	 * <li>Byte<ul><li>前後の空白およびカンマは無視されます。</li></ul></li>
	 * <li>Integer<ul><li>前後の空白およびカンマは無視されます。</li></ul></li>
	 * <li>Long<ul><li>前後の空白およびカンマは無視されます。</li></ul></li>
	 * <li>Float<ul><li>前後の空白およびカンマは無視されます。</li></ul></li>
	 * <li>Double<ul><li>前後の空白およびカンマは無視されます。</li></ul></li>
	 * <li>BigInteger<ul><li>前後の空白およびカンマは無視されます。</li></ul></li>
	 * <li>BigDecimal<ul><li>前後の空白およびカンマは無視されます。</li></ul></li>
	 * <li>LocalDate<ul><li>前後の空白は無視されます。</li><li>書式はyy/M/d または yyyy/M/dです。記号はマイナス"-"またはドット"."でも可能です。</li><li>西暦が2桁の場合、70..99は1900年代、00..69は2000年代と解釈します。</ul></li>
	 * <li>LocalTime<ul><li>前後の空白は無視されます。</li><li>書式はH:m:d.nnnnnnnnnです。nは9桁まで記述でき、省略も可能です。</li></ul></li>
	 * <li>LocalDateTime<ul><li>前後の空白は無視されます。</li><li>書式は、LocalDateの書式とLocalTimeの書式を、空白または"T"で連結したものです。</li></ul></li>
	 * </ul>
	 * 
	 * @param type 解析する型
	 * @param string 解析する文字列
	 * @return 解析結果 （解析する文字列がnullの場合はnull）
	 */
	public static <T> T parse(Class<T> type, String string) {
		return INSTANCE.parseString(type, string);
	}

	/** パーサーマップ。 */
	private final Map<Class<?>, Function<String, ?>> parserMap;

	/**
	 * コンストラクタ。
	 */
	private StringParser() {
		HashMap<Class<?>, Function<String, ?>> map = new HashMap<>();
		map.put(String.class, string -> string);
		map.put(Boolean.class, string -> Boolean.valueOf(string.trim()));
		map.put(Byte.class, string -> Byte.valueOf(normalizeNumber(string)));
		map.put(Integer.class, string -> Integer.valueOf(normalizeNumber(string)));
		map.put(Long.class, string -> Long.valueOf(normalizeNumber(string)));
		map.put(Float.class, string -> Float.valueOf(normalizeNumber(string)));
		map.put(Double.class, string -> Double.valueOf(normalizeNumber(string)));
		map.put(BigInteger.class, string -> new BigInteger(normalizeNumber(string)));
		map.put(BigDecimal.class, string -> new BigDecimal(normalizeNumber(string)));
		map.put(LocalDate.class, string -> parseLocalDate(string, () -> String.format("wrong date format: %s", string)));
		map.put(LocalTime.class, string -> parseLocalTime(string, () -> String.format("wrong time format: %s", string)));
		map.put(LocalDateTime.class, string -> parseLocalDateTime(string, () -> String.format("wrong date-time format: %s", string)));
		parserMap = Collections.unmodifiableMap(map);
	}

	/**
	 * 文字列を解析します。
	 * 
	 * @param type 解析する型
	 * @param string 解析する文字列
	 * @return 解析結果
	 */
	private <T> T parseString(Class<T> type, String string) {
		if (string == null) {
			return null;
		}

		if (!parserMap.containsKey(type)) {
			throw new IllegalArgumentException(String.format("unsupported type: %s", type));
		}

		return type.cast(parserMap.get(type).apply(string));
	}

	/**
	 * 数値表記を標準化します。
	 * 
	 * @param string 標準化する文字列
	 * @return 標準化された文字列
	 */
	private String normalizeNumber(String string) {
		return string.trim().replace(",", "");
	}

	/**
	 * 文字列をタイムゾーンのない日付として解析します。
	 * 
	 * @param string 解析する文字列
	 * @param errorMessage 解析失敗時の例外メッセージ
	 * @return 解析結果
	 */
	private LocalDate parseLocalDate(String string, Supplier<String> errorMessage) {
		Matcher matcher = DATE_PATTERN.matcher(string.trim());

		if (!matcher.matches()) {
			throw new IllegalArgumentException(errorMessage.get());
		}

		int year = Integer.parseInt(matcher.group(1));
		int month = Integer.parseInt(matcher.group(2));
		int dayOfMonth = Integer.parseInt(matcher.group(3));

		if (year < 70) {
			year += 2000;
		} else if (year < 100) {
			year += 1900;
		}

		return LocalDate.of(year, month, dayOfMonth);
	}

	/**
	 * 文字列をタイムゾーンのない時刻として解析します。
	 * 
	 * @param string 解析する文字列
	 * @param errorMessage 解析失敗時の例外メッセージ
	 * @return 解析結果
	 */
	private LocalTime parseLocalTime(String string, Supplier<String> errorMessage) {
		Matcher matcher = TIME_PATTERN.matcher(string.trim());

		if (!matcher.matches()) {
			throw new IllegalArgumentException(errorMessage.get());
		}

		int hour = Integer.parseInt(matcher.group(1));
		int minute = Integer.parseInt(matcher.group(2));
		int second = Integer.parseInt(matcher.group(3));
		int nanoOfSecond = Optional.ofNullable(matcher.group(5))
				.map(value -> (value + "00000000").substring(0, 9))
				.map(Integer::valueOf)
				.orElse(0);

		return LocalTime.of(hour, minute, second, nanoOfSecond);
	}

	/**
	 * 文字列をタイムゾーンのない日付時刻として解析します。
	 * 
	 * @param string 解析する文字列
	 * @return 解析結果
	 */
	private LocalDateTime parseLocalDateTime(String string, Supplier<String> errorMessage) {
		String[] elements = string.trim().split("(\\s+|T)", 2);

		if (elements.length != 2) {
			throw new IllegalArgumentException(errorMessage.get());
		}

		LocalDate localDate = parseLocalDate(elements[0], errorMessage);
		LocalTime localTime = parseLocalTime(elements[1], errorMessage);
		return localDate.atTime(localTime);
	}
}
