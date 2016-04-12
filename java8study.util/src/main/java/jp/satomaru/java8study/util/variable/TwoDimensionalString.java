package jp.satomaru.java8study.util.variable;

import java.util.function.BinaryOperator;

import jp.satomaru.java8study.util.TwoDimensional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 2次元座標を割り振られた文字列の変数です。
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"x", "y"}, callSuper = false)
public class TwoDimensionalString extends StringVariableBase<TwoDimensionalString> implements TwoDimensional {

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
	public TwoDimensionalString(int x, int y, BinaryOperator<String> positionToValue) {
		this.x = x;
		this.y = y;
		setValue(positionToValue.apply(String.valueOf(x), String.valueOf(y)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TwoDimensionalString self() {
		return this;
	}
}
