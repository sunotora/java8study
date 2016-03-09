package jp.satomaru.java8study.util.variable;

import java.util.function.IntFunction;

import jp.satomaru.java8study.util.Indexed;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * インデックス番号を割り振られた変数です。
 *
 * @param <T> 保持する値
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"index"}, callSuper = false)
public class IndexedVariable<T> extends VariableBase<T, IndexedVariable<T>> implements Indexed {

	/** インデックス番号。 */
	@Getter
	private final int index;

	/**
	 * コンストラクタ。
	 * 
	 * @param index インデックス番号
	 * @param indexToValue インデックス番号を受け取り、値を返す関数
	 */
	public IndexedVariable(int index, IntFunction<T> indexToValue) {
		this.index = index;
		setValue(indexToValue.apply(index));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IndexedVariable<T> self() {
		return this;
	}
}
