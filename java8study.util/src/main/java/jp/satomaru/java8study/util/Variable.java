package jp.satomaru.java8study.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 値を保持します。
 *
 * @param <T> 値の型
 * @param <S> サブクラス
 */
public abstract class Variable<T, S extends Variable<T, S>> {

	/** 値。 */
	private Optional<T> valueOptional = Optional.empty();

	/**
	 * 値をOptionalで取得します。
	 * 
	 * @return 値
	 */
	public final Optional<T> optional() {
		return valueOptional;
	}

	/**
	 * 値を消去します。
	 * 
	 * <p>
	 * 値はnullになります。
	 * </p>
	 * 
	 * @return このオブジェクト自身
	 */
	public final S clear() {
		valueOptional = Optional.empty();
		return self();
	}

	/**
	 * 値がnullであることを判定します。
	 * 
	 * @return 値がnullである場合はtrue
	 */
	public final boolean isNull() {
		return !valueOptional.isPresent();
	}

	/**
	 * 値を取得します。
	 * 
	 * @return 値
	 */
	public final T getValue() {
		return valueOptional.orElse(null);
	}

	/**
	 * 値を設定します。
	 * 
	 * @param value 値
	 * @return このオブジェクト自身
	 */
	public final S setValue(T value) {
		valueOptional = Optional.ofNullable(value);
		return self();
	}

	/**
	 * 値を設定します。
	 * 
	 * @param other 設定する値を保持しているVariable
	 * @return このオブジェクト自身
	 */
	public final S setValue(Variable<? extends T, ?> other) {
		valueOptional = Optional.ofNullable(other.getValue());
		return self();
	}

	/**
	 * 値が等しいことを判定します。
	 * 
	 * @param other 比較する値
	 * @return 値が等しい場合はtrue
	 */
	public final boolean isSameValue(Object other) {
		return Objects.equals(getValue(), other);
	}

	/**
	 * 値が等しいことを判定します。
	 * 
	 * @param other 比較する値
	 * @return 値が等しい場合はtrue
	 */
	public final boolean isSameValue(Variable<?, ?> other) {
		return isSameValue(other.getValue());
	}

	/**
	 * 値が等しいことを判定します。
	 * 
	 * @param other 比較する値
	 * @param valueConverter 引数の値を変換する関数（値がnullの場合は使用されない）
	 * @return 値が等しい場合はtrue
	 */
	public final <U> boolean isSameValue(Variable<U, ?> other, Function<U, T> valueConverter) {
		return isSameValue(other.optional().map(valueConverter).orElse(null));
	}

	/**
	 * 値が異なることを判定します。
	 * 
	 * @param other 比較する値
	 * @return 値が異なる場合はtrue
	 */
	public final boolean isNotSameValue(Object other) {
		return !isSameValue(other);
	}

	/**
	 * 値が異なることを判定します。
	 * 
	 * @param other 比較する値
	 * @return 値が異なる場合はtrue
	 */
	public final boolean isNotSameValue(Variable<?, ?> other) {
		return !isSameValue(other);
	}

	/**
	 * 値が異なることを判定します。
	 * 
	 * @param other 比較する値
	 * @param valueConverter 引数の値を変換する関数（値がnullの場合は使用されない）
	 * @return 値が異なる場合はtrue
	 */
	public final <U> boolean isNotSameValue(Variable<U, ?> item, Function<U, T> valueConverter) {
		return !isSameValue(item, valueConverter);
	}

	/**
	 * 値を処理します。
	 * 
	 * @param action 値を処理する関数（値がnullの場合は実行されない）
	 * @return このオブジェクト自身
	 */
	public final S accept(Consumer<? super T> action) {
		valueOptional.ifPresent(action);
		return self();
	}

	/**
	 * 値を処理します。
	 * 
	 * @param action 値を処理する関数（値がnullの場合は実行されない）
	 * @return 処理結果（値が処理されなかった場合はnull）
	 */
	public final <R> R apply(Function<? super T, R> action) {
		return valueOptional.map(action).orElse(null);
	}

	/**
	 * このオブジェクト自身を返却してください。
	 * 
	 * @return このオブジェクト自身
	 */
	protected abstract S self();
}
