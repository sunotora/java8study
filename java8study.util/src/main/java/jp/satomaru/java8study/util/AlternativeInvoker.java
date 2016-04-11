package jp.satomaru.java8study.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * メソッドを実行する時、オブジェクトがそのメソッドが実装していない場合は、代わりとなる関数を実行します。
 * 
 * @param <T> 実行するメソッドが定義されている型
 */
@RequiredArgsConstructor
public class AlternativeInvoker<T> {

	/**
	 * 引数なし、戻り値なしのメソッド用です。
	 * 
	 * @param <U> 実行するオブジェクト
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class NoArgsUnret<U> {
		private final Consumer<T> action;
		private final Consumer<U> defaultAction;

		/**
		 * メソッドを実行します。
		 * 
		 * <p>
		 * 実行するオブジェクトが、実行するメソッドが定義されている型を実装していない場合は、代わりとなる関数を実行します。
		 * </p>
		 * 
		 * @param object 実行するオブジェクト。
		 */
		public void invoke(U object) {
			if (type.isInstance(object)) {
				action.accept(type.cast(object));
			} else {
				defaultAction.accept(object);
			}
		}
	}

	/**
	 * 引数なし、戻り値ありのメソッド用です。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <R> 戻り値
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class NoArgsRet<U, R> {
		private final Function<T, R> action;
		private final Function<U, R> defaultAction;

		/**
		 * メソッドを実行します。
		 * 
		 * <p>
		 * 実行するオブジェクトが、実行するメソッドが定義されている型を実装していない場合は、代わりとなる関数を実行します。
		 * </p>
		 * 
		 * @param object 実行するオブジェクト。
		 * @return 結果
		 */
		public R invoke(U object) {
			if (type.isInstance(object)) {
				return action.apply(type.cast(object));
			} else {
				return defaultAction.apply(object);
			}
		}
	}

	/**
	 * 引数1つ、戻り値なしのメソッド用です。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 引数
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class OneArgUnret<U, X> {
		private final BiConsumer<T, X> action;
		private final BiConsumer<U, X> defaultAction;

		/**
		 * メソッドを実行します。
		 * 
		 * <p>
		 * 実行するオブジェクトが、実行するメソッドが定義されている型を実装していない場合は、代わりとなる関数を実行します。
		 * </p>
		 * 
		 * @param object 実行するオブジェクト。
		 * @param arg 引数
		 */
		public void invoke(U object, X arg) {
			if (type.isInstance(object)) {
				action.accept(type.cast(object), arg);
			} else {
				defaultAction.accept(object, arg);
			}
		}
	}

	/**
	 * 引数1つ、戻り値ありのメソッド用です。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 引数
	 * @param <R> 戻り値
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class OneArgRet<U, X, R> {
		private final BiFunction<T, X, R> action;
		private final BiFunction<U, X, R> defaultAction;

		/**
		 * メソッドを実行します。
		 * 
		 * <p>
		 * 実行するオブジェクトが、実行するメソッドが定義されている型を実装していない場合は、代わりとなる関数を実行します。
		 * </p>
		 * 
		 * @param object 実行するオブジェクト。
		 * @param arg 引数
		 * @return 結果
		 */
		public R invoke(U object, X arg) {
			if (type.isInstance(object)) {
				return action.apply(type.cast(object), arg);
			} else {
				return defaultAction.apply(object, arg);
			}
		}
	}

	/**
	 * 引数2つ、戻り値なしのメソッド用です。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 第1引数
	 * @param <Y> 第2引数
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class TwoArgsUnret<U, X, Y> {
		private final TriConsumer<T, X, Y> action;
		private final TriConsumer<U, X, Y> defaultAction;

		/**
		 * メソッドを実行します。
		 * 
		 * <p>
		 * 実行するオブジェクトが、実行するメソッドが定義されている型を実装していない場合は、代わりとなる関数を実行します。
		 * </p>
		 * 
		 * @param object 実行するオブジェクト。
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 */
		public void invoke(U object, X arg1, Y arg2) {
			if (type.isInstance(object)) {
				action.accept(type.cast(object), arg1, arg2);
			} else {
				defaultAction.accept(object, arg1, arg2);
			}
		}
	}

	/**
	 * 引数2つ、戻り値ありのメソッド用です。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 第1引数
	 * @param <Y> 第2引数
	 * @param <R> 戻り値
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class TwoArgsRet<U, X, Y, R> {
		private final TriFunction<T, X, Y, R> action;
		private final TriFunction<U, X, Y, R> defaultAction;

		/**
		 * メソッドを実行します。
		 * 
		 * <p>
		 * 実行するオブジェクトが、実行するメソッドが定義されている型を実装していない場合は、代わりとなる関数を実行します。
		 * </p>
		 * 
		 * @param object 実行するオブジェクト。
		 * @param arg1 第1引数
		 * @param arg2 第2引数
		 * @return 結果
		 */
		public R invoke(U object, X arg1, Y arg2) {
			if (type.isInstance(object)) {
				return action.apply(type.cast(object), arg1, arg2);
			} else {
				return defaultAction.apply(object, arg1, arg2);
			}
		}
	}

	/** 実行するメソッドが定義されている型。 */
	private final Class<T> type;

	/**
	 * 引数なし、戻り値なしのメソッドを実行する準備をします。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param action 実行するメソッド
	 * @param defaultAction 実行するメソッドが実装されていない場合に、代わりに実行される関数
	 * @return 実行に用いるオブジェクト
	 */
	public <U> NoArgsUnret<U> ready(
			Consumer<T> action,
			Consumer<U> defaultAction) {

		return new NoArgsUnret<>(action, defaultAction);
	}

	/**
	 * 引数なし、戻り値ありのメソッドを実行する準備をします。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <R> 戻り値
	 * @param action 実行するメソッド
	 * @param defaultAction 実行するメソッドが実装されていない場合に、代わりに実行される関数
	 * @return 実行に用いるオブジェクト
	 */
	public <U, R> NoArgsRet<U, R> ready(
			Function<T, R> action,
			Function<U, R> defaultAction) {

		return new NoArgsRet<>(action, defaultAction);
	}

	/**
	 * 引数1つ、戻り値なしのメソッドを実行する準備をします。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 引数
	 * @param action 実行するメソッド
	 * @param defaultAction 実行するメソッドが実装されていない場合に、代わりに実行される関数
	 * @return 実行に用いるオブジェクト
	 */
	public <U, X> OneArgUnret<U, X> ready(
			BiConsumer<T, X> action,
			BiConsumer<U, X> defaultAction) {

		return new OneArgUnret<>(action, defaultAction);
	}

	/**
	 * 引数1つ、戻り値ありのメソッドを実行する準備をします。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 引数
	 * @param <R> 戻り値
	 * @param action 実行するメソッド
	 * @param defaultAction 実行するメソッドが実装されていない場合に、代わりに実行される関数
	 * @return 実行に用いるオブジェクト
	 */
	public <U, X, R> OneArgRet<U, X, R> ready(
			BiFunction<T, X, R> action,
			BiFunction<U, X, R> defaultAction) {

		return new OneArgRet<>(action, defaultAction);
	}

	/**
	 * 引数2つ、戻り値なしのメソッドを実行する準備をします。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 第1引数
	 * @param <Y> 第2引数
	 * @param action 実行するメソッド
	 * @param defaultAction 実行するメソッドが実装されていない場合に、代わりに実行される関数
	 * @return 実行に用いるオブジェクト
	 */
	public <U, X, Y> TwoArgsUnret<U, X, Y> ready(
			TriConsumer<T, X, Y> action,
			TriConsumer<U, X, Y> defaultAction) {

		return new TwoArgsUnret<>(action, defaultAction);
	}

	/**
	 * 引数2つ、戻り値ありのメソッドを実行する準備をします。
	 * 
	 * @param <U> 実行するオブジェクト
	 * @param <X> 第1引数
	 * @param <Y> 第2引数
	 * @param <R> 戻り値
	 * @param action 実行するメソッド
	 * @param defaultAction 実行するメソッドが実装されていない場合に、代わりに実行される関数
	 * @return 実行に用いるオブジェクト
	 */
	public <U, X, Y, R> TwoArgsRet<U, X, Y, R> ready(
			TriFunction<T, X, Y, R> action,
			TriFunction<U, X, Y, R> defaultAction) {

		return new TwoArgsRet<>(action, defaultAction);
	}
}
