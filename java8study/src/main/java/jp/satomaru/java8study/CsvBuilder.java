package jp.satomaru.java8study;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * CSVを作成します。
 *
 * @param <E> CSVを作成する元となるオブジェクト
 */
@RequiredArgsConstructor
@Setter
public class CsvBuilder<E> {

	/** オブジェクトから列を取得する関数。 */
	private final Function<E, Object[]> picker;

	/** 列を文字列に変換する関数のマップ。キーは列の型。 */
	private final Map<Class<?>, Function<Object, String>> formatters = new HashMap<>();

	/** 文字列をクォーテーションする際に、前後に追加する記号。 */
	private String quotation = "\"";

	/** 文字列中に含まれるクォーテーション記号を退避するための文字列。 */
	private String escapedQuotation = "\"\"";

	/** 列の区切り記号。 */
	private String columnSeparator = ",";

	/** 行の区切り記号。 */
	private String lineSeparator = System.getProperty("line.separator");

	/**
	 * 列を文字列に変換する関数を追加します。
	 * 
	 * @param type 列の型
	 * @param formatter 列を文字列に変換する関数
	 * @return このオブジェクト自身
	 */
	@SuppressWarnings("unchecked")
	public <T> CsvBuilder<E> addFormatter(Class<T> type, Function<T, String> formatter) {
		formatters.put(type, (Function<Object, String>) formatter);
		return this;
	}

	/**
	 * CSVを作成します。
	 * 
	 * <p>
	 * 各オブジェクトを{@link #picker}で配列化し、{@link #formatters}で文字列に変換して{@link #columnSeparator}で連結した後、
	 * その各オブジェクトの結果を{@link #lineSeparator}で連結します。
	 * </p>
	 * 
	 * <p>
	 * {@link #formatters}に列の型に対応する関数が追加されていない型の場合、
	 * Number型のサブクラスおよびBoolean型の時は{@link Object#toString()}を使用します。
	 * それ以外の型の時は、{@link Object#toString()}で文字列に変換した後、
	 * 文字列中の{@link #quotation}を{@link #escapedQuotation}に置換して、
	 * 文字列の前後に{@link #quotation}を追加します。
	 * また列の値がnullの時は、型に関係なくブランクになります。
	 * </p>
	 * 
	 * @param entities CSVを作成する元となるオブジェクトのコレクション
	 * @return CSV
	 */
	public String build(Collection<E> entities) {
		return entities.stream()
				// TODO 実装してください。なお、先にcreateRecord(Object[])を実装して、それを利用してください。
	}

	/**
	 * CSVの行を作成します。
	 * 
	 * <p>
	 * 列を{@link #formatters}で文字列に変換した後、{@link #columnSeparator}で連結します。
	 * {@link #formatters}に列の型に対応する関数が追加されていない型の場合は{@link #defaultFormat(Object)}を使用します。
	 * また列がnullの場合はブランクを使用します。
	 * </p>
	 * 
	 * @param columns 列の配列
	 * @return CSVの行
	 */
	private String createRecord(Object[] columns) {
		return Stream.of(columns)
				// TODO 実装してください。
	}

	/**
	 * 列を文字列に変換する関数が存在しない場合のデフォルトフォーマッターです。
	 * 
	 * <p>
	 * Number型のサブクラスおよびBoolean型の時は{@link Object#toString()}を使用します。
	 * それ以外の型の時は、{@link Object#toString()}で文字列に変換した後、
	 * 文字列中の{@link #quotation}を{@link #escapedQuotation}に置換して、
	 * 文字列の前後に{@link #quotation}を追加します。
	 * </p>
	 * 
	 * @param column 列
	 * @return 変換後の文字列
	 */
	private String defaultFormat(Object column) {
		String string = column.toString();

		if (column instanceof Number) {
			return string;
		}

		if (column instanceof Boolean) {
			return string;
		}

		return new StringBuilder(string.length() + quotation.length() * 2 + 16)
				.append(quotation)
				.append(string.replace(quotation, escapedQuotation))
				.append(quotation)
				.toString();
	}
}
