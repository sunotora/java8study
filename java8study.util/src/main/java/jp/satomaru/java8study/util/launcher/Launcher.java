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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * オブジェクトを実行する簡易的なフレームワークです。
 * 
 * @param <T> 実行するオブジェクト
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Launcher<T> {

	/**
	 * 設定を保持します。
	 * 
	 * @param <T> 実行するオブジェクト
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Config<T> {

		/** コマンドマップ。 */
		private final Map<String, BiConsumer<T, Message>> commandMap = new HashMap<>();

		/** コマンドが指定されていない場合に実行される関数。 */
		private BiConsumer<T, Message> whenNoCommand = (model, message) -> {};

		/** 該当するコマンドが存在しない場合に実行される関数。 */
		private TriConsumer<T, Message, String> whenIllegalCommand = (model, message, command) -> {};

		/** インデックスに関連付けられた引数のバリデーション例外が発生した場合に実行される関数。 */
		private TriConsumer<T, Message, InvalidArgument> whenInvalidArgument = (model, message, invalid) -> {};

		/** 名前に関連付けられた引数のバリデーション例外が発生した場合に実行される関数。 */
		private TriConsumer<T, Message, InvalidParameter> whenInvalidParameter = (model, message, invalid) -> {};

		/** エラー処理後に実行される関数。 */
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
		 * エラーハンドラーのひとつである、コマンドが指定されていない場合に実行される関数を設定します。
		 * 
		 * <p>
		 * ただし、実行するオブジェクトに{@link ErrorHandler}が実装されている場合は、
		 * 実行するオブジェクトに実装されている{@link ErrorHandler#whenNoCommand(Message)}が優先的に実行されます。
		 * </p>
		 * 
		 * @param whenNoCommand 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenNoCommand(BiConsumer<T, Message> whenNoCommand) {
			this.whenNoCommand = whenNoCommand;
			return this;
		}

		/**
		 * エラーハンドラーのひとつである、該当するコマンドが存在しない場合に実行される関数を設定します。
		 * 
		 * <p>
		 * ただし、実行するオブジェクトに{@link ErrorHandler}が実装されている場合は、
		 * 実行するオブジェクトに実装されている{@link ErrorHandler#whenIllegalCommand(Message, String)}が優先的に実行されます。
		 * </p>
		 * 
		 * @param whenIllegalCommand 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenIllegalCommand(TriConsumer<T, Message, String> whenIllegalCommand) {
			this.whenIllegalCommand = whenIllegalCommand;
			return this;
		}

		/**
		 * エラーハンドラーのひとつである、インデックスに関連付けられた引数のバリデーション例外が発生した場合に実行される関数を設定します。
		 * 
		 * <p>
		 * ただし、実行するオブジェクトに{@link ErrorHandler}が実装されている場合は、
		 * 実行するオブジェクトに実装されている{@link ErrorHandler#whenInvalidArgument(Message, InvalidArgument)}が優先的に実行されます。
		 * </p>
		 * 
		 * @param whenInvalidArgument 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenInvalidArgument(TriConsumer<T, Message, InvalidArgument> whenInvalidArgument) {
			this.whenInvalidArgument = whenInvalidArgument;
			return this;
		}

		/**
		 * エラーハンドラーのひとつである、名前に関連付けられた引数のバリデーション例外が発生した場合に実行される関数を設定します。
		 * 
		 * <p>
		 * ただし、実行するオブジェクトに{@link ErrorHandler}が実装されている場合は、
		 * 実行するオブジェクトに実装されている{@link ErrorHandler#whenInvalidParameter(Message, InvalidParameter)}が優先的に実行されます。
		 * </p>
		 * 
		 * @param whenInvalidParameter 実行される関数
		 * @return このオブジェクト自身
		 */
		public Config<T> whenInvalidParameter(TriConsumer<T, Message, InvalidParameter> whenInvalidParameter) {
			this.whenInvalidParameter = whenInvalidParameter;
			return this;
		}

		/**
		 * エラーハンドラーのひとつである、エラー処理後に実行される関数を設定します。
		 * 
		 * <p>
		 * ただし、実行するオブジェクトに{@link ErrorHandler}が実装されている場合は、
		 * 実行するオブジェクトに実装されている{@link ErrorHandler#whenError(Message)}が優先的に実行されます。
		 * </p>
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

	/** コマンドマップ。 */
	private final Map<String, BiConsumer<T, Message>> commandMap;

	/** コマンドが指定されていない場合に実行される関数。 */
	private final BiConsumer<T, Message> whenNoCommand;

	/** 該当するコマンドが存在しない場合に実行される関数。 */
	private final TriConsumer<T, Message, String> whenIllegalCommand;

	/** インデックスに関連付けられた引数のバリデーション例外が発生した場合に実行される関数。 */
	private final TriConsumer<T, Message, InvalidArgument> whenInvalidArgument;

	/** 名前に関連付けられた引数のバリデーション例外が発生した場合に実行される関数。 */
	private final TriConsumer<T, Message, InvalidParameter> whenInvalidParameter;

	/** エラー処理後に実行される関数。 */
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
			handle(model, message, ErrorHandler::whenNoCommand, whenNoCommand);
			handle(model, message, ErrorHandler::whenError, whenError);
			return;
		}

		String command = request.getCommand().get();

		if (!commandMap.containsKey(command)) {
			handle(model, message, command, ErrorHandler::whenIllegalCommand, whenIllegalCommand);
			handle(model, message, ErrorHandler::whenError, whenError);
			return;
		}

		try {
			commandMap.get(command).accept(model, message);
		} catch (InvalidArgument e) {
			handle(model, message, e, ErrorHandler::whenInvalidArgument, whenInvalidArgument);
			handle(model, message, ErrorHandler::whenError, whenError);
		} catch (InvalidParameter e) {
			handle(model, message, e, ErrorHandler::whenInvalidParameter, whenInvalidParameter);
			handle(model, message, ErrorHandler::whenError, whenError);
		}
	}

	/**
	 * エラ－ハンドリングを行います。
	 * 
	 * <p>
	 * 実行するオブジェクトが{@link ErrorHandler}を実装している場合は、
	 * 実行するオブジェクトに実装されているハンドラーを実行します。
	 * そうでない場合は、ランチャーに設定されているハンドラーを実行します。
	 * </p>
	 * 
	 * @param model 実行するオブジェクト
	 * @param message メッセージ
	 * @param action 実行するオブジェクトに実装されているハンドラー
	 * @param defaultAction ランチャーに設定されているハンドラー
	 */
	private void handle(
			T model,
			Message message,
			BiConsumer<ErrorHandler, Message> action,
			BiConsumer<T, Message> defaultAction) {

		if (model instanceof ErrorHandler) {
			action.accept((ErrorHandler) model, message);
		} else {
			defaultAction.accept(model, message);
		}
	}

	/**
	 * エラ－ハンドリングを行います。
	 * 
	 * <p>
	 * 実行するオブジェクトが{@link ErrorHandler}を実装している場合は、
	 * 実行するオブジェクトに実装されているハンドラーを実行します。
	 * そうでない場合は、ランチャーに設定されているハンドラーを実行します。
	 * </p>
	 * 
	 * @param model 実行するオブジェクト
	 * @param message メッセージ
	 * @param argument 引数
	 * @param action 実行するオブジェクトに実装されているハンドラー
	 * @param defaultAction ランチャーに設定されているハンドラー
	 */
	private <U> void handle(
			T model,
			Message message,
			U argument,
			TriConsumer<ErrorHandler, Message, U> action,
			TriConsumer<T, Message, U> defaultAction) {

		if (model instanceof ErrorHandler) {
			action.accept((ErrorHandler) model, message, argument);
		} else {
			defaultAction.accept(model, message, argument);
		}
	}
}
