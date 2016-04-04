package jp.satomaru.java8study.util.launcher.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import jp.satomaru.java8study.util.launcher.StringParser;
import lombok.Getter;

/**
 * main関数の引数から作成する、モデルへの要求です。
 */
public class StringsRequest implements Request {

	/** 名前に関連付けられた引数のパターン。グループ1が名前、グループ3が値。 */
	public static final Pattern PARAMETER_PATTERN = Pattern.compile("/(\\w+)(:(.+))?");

	/** コマンド。 */
	@Getter private final Optional<String> command;

	/** インデックスに関連付けられた引数のリスト。 */
	private final List<String> arguments = new ArrayList<>();

	/** 名前に関連付けられた引数のマップ。 */
	private final Map<String, String> parameters = new HashMap<>();

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 引数の文字列配列の、最初の要素をコマンドとみなします。
	 * それ以降は、"/(\\w+)(:(.+))?"の正規表現に一致するものを名前に関連付けられた引数、
	 * 一致しないものをインデックスに関連付けられた引数とみなします。
	 * </p>
	 * 
	 * @param args 文字列配列
	 */
	public StringsRequest(String[] args) {
		if (args == null || args.length == 0) {
			command = Optional.empty();
		} else {
			command = Optional.ofNullable(args[0]);

			Stream.of(args)
				.skip(1)
				.filter(Objects::nonNull)
				.forEach(string -> {
					Matcher matcher = PARAMETER_PATTERN.matcher(string);

					if (matcher.matches()) {
						parameters.put(matcher.group(1), matcher.group(3));
					} else {
						arguments.add(string);
					}
				});
		}
	}

	/**
	 * インデックスに関連付けられた引数を取得します。
	 * 
	 * <p>
	 * {@link StringParser#parse(Class, String)}を使用して、
	 * main関数の引数を指定された型に変換します。
	 * 変換に失敗した場合は例外が送出されます。
	 * </p>
	 * 
	 * @param index 引数のインデックス
	 * @param type 引数の型
	 * @return 引数（存在しない場合はempty）
	 */
	@Override
	public <T> Optional<T> get(int index, Class<T> type) {
		return (arguments.size() > index)
				? Optional.of(arguments.get(index)).map(string -> StringParser.parse(type, string))
				: Optional.empty();
	}

	/**
	 * 名前に関連付けられた引数を取得します。
	 * 
	 * <p>
	 * {@link StringParser#parse(Class, String)}を使用して、
	 * main関数の引数を指定された型に変換します。
	 * 変換に失敗した場合は例外が送出されます。
	 * なお、"/p"というように、値が指定されていない場合もemptyとなります。
	 * </p>
	 * 
	 * @param name 引数の名前
	 * @param type 引数の型
	 * @return 引数（存在しない場合はempty）
	 */
	@Override
	public <T> Optional<T> get(String name, Class<T> type) {
		return (parameters.containsKey(name))
				? Optional.ofNullable(parameters.get(name)).map(string -> StringParser.parse(type, string))
				: Optional.empty();
	}

	/**
	 * インデックスに関連付けられた引数の個数を取得します。
	 * 
	 * @return インデックスに関連付けられた引数の個数
	 */
	public int getArgumentsSize() {
		return arguments.size();
	}

	/**
	 * 名前に関連付けられた引数が存在することを判定します。
	 * 
	 * @param name 引数の名前
	 * @return 存在する場合はtrue（値がemptyの場合も含む）
	 */
	public boolean isParameterExisted(String name) {
		return parameters.containsKey(name);
	}
}
