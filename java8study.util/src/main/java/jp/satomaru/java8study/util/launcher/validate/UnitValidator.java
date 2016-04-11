package jp.satomaru.java8study.util.launcher.validate;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

/**
 * リクエストの単項目バリデーションを行います。
 * 
 * @param <K> 対象となる引数のキー
 * @param <V> 対象となる引数の型
 */
@RequiredArgsConstructor
public class UnitValidator<K, V> {

	/** バリデーターコンテキスト。 */
	private final ValidatorContext<K, V> context;

	/** 引数の値。 */
	private final Optional<V> optional;

	/**
	 * 引数が存在することを判定します。
	 * 
	 * @param messageSupplier 引数が存在しなかった場合に送出されるバリデーション例外のメッセージを作成する関数
	 * @return このオブジェクト自身
	 */
	public UnitValidator<K, V> exists(Supplier<String> messageSupplier) {
		optional.orElseThrow(() -> context.createInvalid(messageSupplier));
		return this;
	}

	/**
	 * 引数が妥当であることを判定します。
	 * 
	 * @param unexpected 引数が妥当でないことを判定する関数
	 * @param messageSupplier 引数が妥当でない場合に送出されるバリデーション例外のメッセージを作成する関数
	 * @return このオブジェクト自身
	 */
	public UnitValidator<K, V> when(Predicate<V> unexpected, Supplier<String> messageSupplier) {
		optional.ifPresent(value -> {
			if (unexpected.test(value)) {
				throw context.createInvalid(messageSupplier);
			}
		});

		return this;
	}

	/**
	 * バリデーションを終了して、引数の値を取得します。
	 * 
	 * @return 引数の値
	 */
	public V end() {
		return optional.orElse(null);
	}
}
