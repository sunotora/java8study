package jp.satomaru.java8study.util.launcher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Launcher<T> {

	private static class Context<T> {
		private Map<String, BiConsumer<T, Request>> commandMap = new HashMap<>();
		private Runnable whenNoCommand = () -> System.err.println("コマンドを指定してください。");
		private Consumer<String> whenIllegalCommand = command -> System.err.println(String.format("不正なコマンドです: %s", command));
		private Consumer<Validator.Invalid> whenInvalid = exception -> System.err.println(exception.getMessage());
	}

	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Builder<T> {
		private final Context<T> context = new Context<>();

		public Builder<T> setCommand(String command, BiConsumer<T, Request> action) {
			context.commandMap.put(command, action);
			return this;
		}

		public Builder<T> whenNoCommand(Runnable whenNoCommand) {
			context.whenNoCommand = whenNoCommand;
			return this;
		}

		public Builder<T> whenIllegalCommand(Consumer<String> whenIllegalCommand) {
			context.whenIllegalCommand = whenIllegalCommand;
			return this;
		}

		public Builder<T> whenInvalid(Consumer<Validator.Invalid> whenInvalid) {
			context.whenInvalid = whenInvalid;
			return this;
		}

		public Launcher<T> ready() {
			return new Launcher<>(context);
		}
	}

	public static <T> Builder<T> of(Class<T> type) {
		return new Builder<>();
	}

	private final Context<T> context;

	public void launch(T model, Request request) {
		if (!request.getCommand().isPresent()) {
			context.whenNoCommand.run();
			return;
		}

		String command = request.getCommand().get();

		if (!context.commandMap.containsKey(command)) {
			context.whenIllegalCommand.accept(command);
			return;
		}

		try {
			context.commandMap.get(command).accept(model, request);
		} catch (Validator.Invalid e) {
			context.whenInvalid.accept(e);
		}
	}
}
