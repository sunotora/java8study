package jp.satomaru.java8study.util.variable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 変数の基底クラスです。
 *
 * @param <T> 保持する値
 * @param <S> サブクラス
 */
public abstract class VariableBase<T, S extends VariableBase<T, S>> {

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
	public final S setValue(VariableBase<? extends T, ?> other) {
		valueOptional = Optional.ofNullable(other.getValue());
		return self();
	}

	/**
	 * 値を設定します。
	 * 
	 * @param action 現在の値を受け取り、設定する値を返す関数。
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 現在の値がnullの場合
	 */
	public final S setValue(UnaryOperator<T> action) {
		return setValue(action.apply(valueOptional.get()));
	}

	/**
	 * 値を設定します。
	 * 
	 * @param other 相手のVariable
	 * @param action 自身および相手の現在の値を受け取り、設定する値を返す関数。
	 * @return このオブジェクト自身
	 * @throws NoSuchElementException 自身または相手の現在の値がnullの場合
	 */
	public final S setValue(VariableBase<? extends T, ?> other, BinaryOperator<T> action) {
		return setValue(
				action.apply(
						valueOptional.orElseThrow(() -> new NoSuchElementException("my value")),
						other.valueOptional.orElseThrow(() -> new NoSuchElementException("other's value"))));
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
	public final boolean isSameValue(VariableBase<?, ?> other) {
		return isSameValue(other.getValue());
	}

	/**
	 * 値が等しいことを判定します。
	 * 
	 * @param <U> 比較する値の型
	 * @param other 比較する値
	 * @param valueConverter 引数の値を変換する関数（値がnullの場合は使用されない）
	 * @return 値が等しい場合はtrue
	 */
	public final <U> boolean isSameValue(VariableBase<U, ?> other, Function<U, T> valueConverter) {
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
	public final boolean isNotSameValue(VariableBase<?, ?> other) {
		return !isSameValue(other);
	}

	/**
	 * 値が異なることを判定します。
	 * 
	 * @param <U> 比較する値の型
	 * @param other 比較する値
	 * @param valueConverter 引数の値を変換する関数（値がnullの場合は使用されない）
	 * @return 値が異なる場合はtrue
	 */
	public final <U> boolean isNotSameValue(VariableBase<U, ?> other, Function<U, T> valueConverter) {
		return !isSameValue(other, valueConverter);
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
	 * @param <R> 処理結果の型
	 * @param action 値を処理する関数（値がnullの場合は実行されない）
	 * @return 処理結果（値が処理されなかった場合はnull）
	 */
	public final <R> R apply(Function<? super T, R> action) {
		return valueOptional.map(action).orElse(null);
	}

	/**
	 * 文字列表現を取得します。
	 * 
	 * @return 保持する値の文字列表現（nullの場合は"null"）
	 */
	@Override
	public String toString() {
		return valueOptional.map(Object::toString).orElse("null");
	}

	/**
	 * このオブジェクト自身を返却してください。
	 * 
	 * @return このオブジェクト自身
	 */
	protected abstract S self();
}
