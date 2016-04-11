package jp.satomaru.java8study.util.launcher.validate;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.Setter;

/**
 * バリデーションに関する情報を保持します。
 * 
 * @param <K> 対象となる引数のキー
 * @param <V> 対象となる引数の型
 */
@Getter
@Setter
public class ValidatorContext<K, V> {

	/** 引数に関連付けられたキー。 */
	private K key;

	/** 引数の型。 */
	private Class<V> type;

	/** バリデーション例外を生成する関数。 */
	private BiFunction<K, String, ? extends Invalid> invalidSupplier;

	/**
	 * バリデーション例外を作成します。
	 * 
	 * @param messageSupplier メッセージを作成する関数
	 * @return バリデーション例外
	 */
	public Invalid createInvalid(Supplier<String> messageSupplier) {
		return invalidSupplier.apply(key, messageSupplier.get());
	}

	/**
	 * リクエストから引数を取得します。
	 * 
	 * @param getter キーと型から引数を取得する関数（通常はRequest::get）
	 * @return 引数
	 */
	public Optional<V> getValue(BiFunction<K, Class<V>, Optional<V>> getter) {
		return getter.apply(key, type);
	}
}
