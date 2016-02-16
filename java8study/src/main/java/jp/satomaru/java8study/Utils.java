package jp.satomaru.java8study;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.JarURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ユーティリティ。
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
	 * @param count 繰り返し回数
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
	public static Map<String, String> toMap(List<String> list) {
		return list.stream()                                                    // ListからStreamを生成。
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
		return map.entrySet().stream()                                          // mapからEntryを取得しストリーム化
				.map(Tuple::of)                                                 // EntryをTuple<Key, Value>に
				.map(tuple -> tuple.map2nd(Utils::defaultString))               // Tupleの２要素目がnullだったら空文字に変更
				.map(tuple -> tuple.format("%s:%s"))                            // Tupleの第一要素 + ":" + 第二要素の文字列に変換
				.collect(Collectors.toList());                                  // 文字列を集計してリストに
	}

	/**
	 * コレクション内のオブジェクトを、型ごとに集めます。
	 *
	 * @param collection コレクション
	 * @return 型ごとに集めた結果を格納したマップ
	 */
	public static Map<Class<?>, List<Object>> groupByType(Collection<?> collection) {
		return collection.stream()                                      // collectionをストリーム化
				.filter(Objects::nonNull)                               // nullでない要素のみにフィルター
				.collect(Collectors.groupingBy(Object::getClass));      // クラスをキーにして要素をグルーピング
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

	/**
	 * 指定された日と曜日の組み合わせのうち、最も今日に近い過去日を検索します。
	 *
	 * @param dayOfMonth 日
	 * @param dayOfWeek 曜日
	 * @return 検索された日
	 */
	public static LocalDate lookForRecentDayOf(int dayOfMonth, DayOfWeek dayOfWeek) {

		LocalDate now = LocalDate.now();

//		return Stream.iterate(now.withDayOfMonth(1), date -> date.minusMonths(1))    // 本日の年月初日から、一月ずつ遡っていくストリームを生成する
//			.filter(date -> checkLastDate(date, dayOfMonth))                         // 末日が引数日より小さい月をフィルター
//			.filter(date -> check(date, dayOfMonth, dayOfWeek))                      // 該当日の曜日・過去日でないかをチェック
//			.findFirst()                                                             // 最初の１件を探索し返却
//			.get();

		return Stream.iterate(now.withDayOfMonth(1), date -> date.minusMonths(1))    // 本日の年月初日から、一月ずつ遡っていくストリームを生成する
				.filter(date -> date.lengthOfMonth() >= dayOfMonth)                  // 月末日が対象引数日以上の月を選択
				.map(date -> date.withDayOfMonth(dayOfMonth))                        // 年月初日から引数日にマップ
				.filter(date -> date.getDayOfWeek().equals(dayOfWeek))               // 引数曜日と同じ日を選択
				.filter(date -> date.isBefore(now))                                  // 過去日を選択
				.findFirst()                                                         // 最初を選択し返却
				.get();
	}

	/**
	 * クラスパスに存在するJARから、指定されたパッケージ配下にある全クラスを検索します。
	 * 
	 * <p>
	 * サブパッケージも全て検索します。
	 * なお、ブートストラップ・クラスローダーが読み込んでいるクラスは検索できないことに注意してください。
	 * </p>
	 * 
	 * @param packageName パッケージ名
	 * @return 検索されたクラスのセット
	 * @throws IOException JARファイルの読み込みに失敗した場合
	 */
	public static Set<Class<?>> searchClassInJar(String packageName) throws IOException {
		String resourceName = packageName.replace('.', '/') + "/";
		URL jarUrl = Thread.currentThread().getContextClassLoader().getResource(resourceName);
		JarURLConnection connection = (JarURLConnection) jarUrl.openConnection();

		try (JarFile jar = connection.getJarFile()) {
			return jar.stream()
					// TODO 実装してください。
		}
	}

	/**
	 * クラスを読み込みます。
	 * 
	 * @param fqcn 完全修飾クラス名（バイナリー名）
	 * @return クラス
	 */
	private static Class<?> loadClass(String fqcn) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(fqcn);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private Utils() {}
}
