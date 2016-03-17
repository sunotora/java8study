package jp.satomaru.java8study.util.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import jp.satomaru.java8study.util.StringParser;
import lombok.Getter;

public class StringsRequest implements Request {

	public static final Pattern PARAMETER_PATTERN = Pattern.compile("/(\\w+)(:(.+))?");

	@Getter private final Optional<String> command;
	private final List<String> arguments = new ArrayList<>();
	private final Map<String, String> parameters = new HashMap<>();

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

	@Override
	public <T> Optional<T> get(Class<T> type, int index) {
		return (arguments.size() > index)
				? Optional.of(arguments.get(index)).map(string -> StringParser.parse(type, string))
				: Optional.empty();
	}

	@Override
	public <T> Optional<T> get(Class<T> type, String name) {
		return (parameters.containsKey(name))
				? Optional.of(parameters.get(name)).map(string -> StringParser.parse(type, string))
				: Optional.empty();
	}

	@Override
	public boolean contains(int index) {
		return arguments.size() > index;
	}

	@Override
	public boolean contains(String name) {
		return parameters.containsKey(name);
	}

	@Override
	public int size() {
		return arguments.size();
	}
}
