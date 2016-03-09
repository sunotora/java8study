package jp.satomaru.java8study.util.variable;

import java.util.function.BinaryOperator;

import jp.satomaru.java8study.util.TwoDimensional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 2次元座標を割り振られた整数の変数です。
 *
 * @param <T> 保持する値
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"x", "y"}, callSuper = false)
public class TwoDimensionalInteger extends IntegerVariableBase<TwoDimensionalInteger> implements TwoDimensional {

	/** X座標。 */
	@Getter
	private final int x;

	/** Y座標。 */
	@Getter
	private final int y;

	/**
	 * コンストラクタ。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @param positionToValue X座標およびX座標を受け取り、値を返す関数
	 */
	public TwoDimensionalInteger(int x, int y, BinaryOperator<Integer> positionToValue) {
		this.x = x;
		this.y = y;
		setValue(positionToValue.apply(x, y));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TwoDimensionalInteger self() {
		return this;
	}
}
