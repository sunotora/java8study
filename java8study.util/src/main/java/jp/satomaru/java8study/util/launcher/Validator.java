package jp.satomaru.java8study.util.launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class Validator {

	public static class Invalid extends RuntimeException {

		private static final long serialVersionUID = 1L;

		@Getter private final String name;

		public Invalid(String name, String message) {
			super(message);
			this.name = name;
		}
	}

	private final String name;
	private final List<Consumer<RuntimeException>> catcherList = new ArrayList<>();
	private Function<String, String> defaultMessageFunction;

	public Validator when(Predicate<RuntimeException> catcher, Function<String, String> messageSupplier) {
		catcherList.add(exception -> {
			if (catcher.test(exception)) {
				throw new Invalid(name, messageSupplier.apply(name));
			}
		});

		return this;
	}

	public Validator when(Class<? extends RuntimeException> catchee, Function<String, String> messageSupplier) {
		return when(exception -> catchee.isInstance(exception), messageSupplier);
	}

	public Validator whenNoSuchElement(Function<String, String> messageSupplier) {
		return when(NoSuchElementException.class, messageSupplier);
	}

	public Validator defaultMessage(Function<String, String> defaultMessageFunction) {
		this.defaultMessageFunction = defaultMessageFunction;
		return this;
	}

	public <T> T test(Supplier<T> testee) {
		try {
			return testee.get();
		} catch (RuntimeException e) {
			catcherList.forEach(catchee -> catchee.accept(e));

			throw new Invalid(name, 
					Optional.ofNullable(defaultMessageFunction)
							.map(function -> function.apply(name))
							.orElse(e.getMessage()));
		}
	}
}
