package jp.satomaru.java8study.util.variable;

import java.util.function.IntFunction;

import jp.satomaru.java8study.util.Indexed;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * インデックス番号を割り振られた整数の変数です。
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"index"}, callSuper = false)
public class IndexedInteger extends IntegerVariableBase<IndexedInteger> implements Indexed {

	/** インデックス番号。 */
	@Getter
	private final int index;

	/**
	 * コンストラクタ。
	 * 
	 * @param index インデックス番号
	 * @param indexToValue インデックス番号を受け取り、値を返す関数
	 */
	public IndexedInteger(int index, IntFunction<Integer> indexToValue) {
		this.index = index;
		setValue(indexToValue.apply(index));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IndexedInteger self() {
		return this;
	}
}
