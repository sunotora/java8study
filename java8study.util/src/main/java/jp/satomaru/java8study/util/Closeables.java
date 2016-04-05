package jp.satomaru.java8study.util;

import java.util.Optional;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Closeableに関するユーティリティです。
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Closeables {

	/**
	 * 対象がCloseableの場合、実行後に自動でクローズを呼び出します。
	 * 
	 * <p>
	 * 対象がCloseableでない場合、クローズは呼び出しません。
	 * </p>
	 * 
	 * @param <T> 対象の型
	 * @param target 対象
	 * @param action 対象を実行する関数
	 * @throws Exception 例外が発生した場合
	 */
	public static <T> void autoClose(T target, Consumer<T> action) throws Exception {
		if (target instanceof AutoCloseable) {
			try (AutoCloseable closeable = (AutoCloseable) target) {
				action.accept(target);
			}
		} else {
			action.accept(target);
		}
	}

	/**
	 * 対象がCloseableの場合、実行後に自動でクローズを呼び出します。
	 * 
	 * <p>
	 * 例外が発生した場合は、実行時例外でラッピングして送出します。
	 * </p>
	 * 
	 * @param <T> 対象の型
	 * @param target 対象
	 * @param action 対象を実行する関数
	 */
	public static <T> void autoCloseSecretly(T target, Consumer<T> action) {
		try {
			autoClose(target, action);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 対象がCloseableの場合、実行後に自動でクローズを呼び出します。
	 * 
	 * <p>
	 * 例外が発生した場合は、送出せずに戻り値として返却します。
	 * </p>
	 * 
	 * @param <T> 対象の型
	 * @param target 対象
	 * @param action 対象を実行する関数
	 * @return 発生した例外
	 */
	public static <T> Optional<Exception> autoCloseSilently(T target, Consumer<T> action) {
		try {
			autoClose(target, action);
			return Optional.empty();
		} catch (Exception e) {
			return Optional.of(e);
		}
	}
}
