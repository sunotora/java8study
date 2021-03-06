package jp.satomaru.java8study;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
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
	 * JARファイルから、指定されたパッケージ配下にある全クラスを検索します。
	 *
	 * <p>
	 * サブパッケージも全て検索します。
	 * </p>
	 *
	 * @param file JARファイル
	 * @param packageName パッケージ名
	 * @return 検索されたクラスの完全修飾名（バイナリー名）のセット
	 * @throws IOException JARファイルの読み込みに失敗した場合
	 */
	public static Set<String> searchClassInJar(File file, String packageName) throws IOException {
		try (JarFile jar = new JarFile(file)) {
			/*
			 * なお、「クラスの完全修飾名」とは、以下を全て満たすものです。
			 * 1. 拡張子が".class"である。
			 * 2. "package-info.class"ではない。
			 * 動作確認は、searchClassInJar(new File("【mavenリポジトリ】", "lombok-1.16.6.jar"), "lombok")でやってみてください。
			 */

//			return jar.stream()                                     // JarEntryのストリームを取得
//				.map(jarEntry -> jarEntry.getName())                // entry中のファイル名を取得
//				.filter(str -> str.endsWith(".class"))              // 拡張子が.classのものを選択
//				.filter(str -> !str.endsWith("package-info.class")) // package-info.classではないものを選択
//				.filter(str -> str.startsWith(packageName + "/"))   // 引数パッケージから始まるものを選択
//				.map(str -> str.replace("/", "."))                  // "/"を"."に変換
//				.map(str -> str.substring(0, str.indexOf(".class")))// 先頭から”.classよリ前を取得
//				.collect(Collectors.toSet());                       // Setに集計


			String suffix = ".class";
			String prefix = packageName.replace('.', '/') + "/";                     // packageName中のドットをスラッシュに置換しておかないと、filterでヒットしません。
			                                                                         // "lombok.launch"等のテストケースが漏れてましたね。
			                                                                         // また、streamに依存しない処理は、予め済ませておきましょう。速度が向上します。

			return jar.stream()
				.map(JarEntry::getName)
				.filter(name -> name.startsWith(prefix)                              // 最も絞り込みが期待できる評価式から実行しましょう
					&& name.endsWith(suffix)                                         // streamメソッドの数を減らすのが容易である場合は、減らした方が速度が向上します。
					&& !name.endsWith("package-info.class"))                         // 可読性と相談しながら、streamメソッドの数を減らすことを検討してください。
				.map(name -> name.substring(0, name.length() - suffix.length())      // 文字列の長さが変化してなく、かつ末尾がsuffixであることは、保証されています。
					.replace('/', '.'))                                              // replace(CharSequence,CharSequence)よりも、こっちの方が高速です。
				.collect(Collectors.toSet());
		}
	}

	private Utils() {}
}
