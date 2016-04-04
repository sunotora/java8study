package jp.satomaru.java8study.util.launcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import jp.satomaru.java8study.util.TriConsumer;
import jp.satomaru.java8study.util.launcher.message.Message;
import jp.satomaru.java8study.util.launcher.message.Request;
import jp.satomaru.java8study.util.launcher.message.Response;
import jp.satomaru.java8study.util.launcher.validate.InvalidArgument;
import jp.satomaru.java8study.util.launcher.validate.InvalidParameter;
import lombok.RequiredArgsConstructor;

/**
 * オブジェクトを実行する簡易的なフレームワークです。
 * 
 * @param <T> 実行するオブジェクト
 */
@RequiredArgsConstructor
public class Launcher<T> {

	/**
	 * 設定を保持します。
	 * 
	 * @param <T> 実行するオブジェクト
	 */
	public static class Config<T> {

		private Map<String, BiConsumer<T, Message>> commandMap = new HashMap<>();
		private BiConsumer<T, Message> whenNoCommand = (model, message) -> {};
		private TriConsumer<T, Message, String> whenIllegalCommand = (model, message, command) -> {};
		private TriConsumer<T, Message, InvalidArgument> whenInvalidArgument = (model, message, invalid) -> {};
		private TriConsumer<T, Message, InvalidParameter> whenInvalidParameter = (model, message, invalid) -> {};
		private BiConsumer<T, Message> whenError = (model, message) -> {};

		/**
		 * コマンドを、実行する関数と関連付けて設定します。
		 * 
		 * @param command コマンド
		 * @param action オブジェクトを実行する関数
		 * @return このオブジェクト自身
		 */
		public Config<T> setCommand(String command, BiConsumer<T, Message> action) {
			commandMap.put(command, action);
			return this;
		}

		/**
		 * コマンドが指定されていない場合に実行される関数を設定します。
		 * 
		 * @param whenNoCommand 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenNoCommand(BiConsumer<T, Message> whenNoCommand) {
			this.whenNoCommand = whenNoCommand;
			return this;
		}

		/**
		 * 該当するコマンドが存在しない場合に実行される関数を設定します。
		 * 
		 * @param whenIllegalCommand 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenIllegalCommand(TriConsumer<T, Message, String> whenIllegalCommand) {
			this.whenIllegalCommand = whenIllegalCommand;
			return this;
		}

		/**
		 * インデックスに関連付けられた引数のバリデーション例外が発生した場合に実行される関数を設定します。
		 * 
		 * @param whenInvalidArgument 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenInvalidArgument(TriConsumer<T, Message, InvalidArgument> whenInvalidArgument) {
			this.whenInvalidArgument = whenInvalidArgument;
			return this;
		}

		/**
		 * 名前に関連付けられた引数のバリデーション例外が発生した場合に実行される関数を設定します。
		 * 
		 * @param whenInvalidParameter 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenInvalidParameter(TriConsumer<T, Message, InvalidParameter> whenInvalidParameter) {
			this.whenInvalidParameter = whenInvalidParameter;
			return this;
		}

		/**
		 * エラー処理後に実行される関数を設定します。
		 * 
		 * @param whenError エラーが発生した後に実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenError(BiConsumer<T, Message> whenError) {
			this.whenError = whenError;
			return this;
		}

		/**
		 * 設定を終了して、ランチャーを生成します。
		 * 
		 * @return ランチャー
		 */
		public Launcher<T> ready() {
			return new Launcher<>(
					Collections.unmodifiableMap(commandMap),
					whenNoCommand,
					whenIllegalCommand,
					whenInvalidArgument,
					whenInvalidParameter,
					whenError);
		}
	}

	/**
	 * 設定を開始します。
	 * 
	 * @param type 実行するオブジェクトのクラス
	 * @return コンフィグ
	 */
	public static <T> Config<T> of(Class<T> type) {
		return new Config<>();
	}

	private final Map<String, BiConsumer<T, Message>> commandMap;
	private final BiConsumer<T, Message> whenNoCommand;
	private final TriConsumer<T, Message, String> whenIllegalCommand;
	private final TriConsumer<T, Message, InvalidArgument> whenInvalidArgument;
	private final TriConsumer<T, Message, InvalidParameter> whenInvalidParameter;
	private final BiConsumer<T, Message> whenError;

	/**
	 * オブジェクトを実行します。
	 * 
	 * @param model 実行するオブジェクト
	 * @param request リクエスト
	 */
	public void launch(T model, Request request, Response response) {
		Message message = new Message(request, response);

		if (!request.getCommand().isPresent()) {
			whenNoCommand.accept(model, message);
			whenError.accept(model, message);
			return;
		}

		String command = request.getCommand().get();

		if (!commandMap.containsKey(command)) {
			whenIllegalCommand.accept(model, message, command);
			whenError.accept(model, message);
			return;
		}

		try {
			commandMap.get(command).accept(model, message);
		} catch (InvalidArgument e) {
			whenInvalidArgument.accept(model, message, e);
			whenError.accept(model, message);
		} catch (InvalidParameter e) {
			whenInvalidParameter.accept(model, message, e);
			whenError.accept(model, message);
		}
	}
}
