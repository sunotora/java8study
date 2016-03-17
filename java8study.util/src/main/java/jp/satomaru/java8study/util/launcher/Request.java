package jp.satomaru.java8study.util.launcher;

import java.util.Optional;

public interface Request {

	Optional<String> getCommand();

	<T> Optional<T> get(Class<T> type, int index);

	<T> Optional<T> get(Class<T> type, String name);

	boolean contains(int index);

	boolean contains(String name);

	int size();
}
