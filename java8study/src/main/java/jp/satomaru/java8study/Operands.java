package jp.satomaru.java8study;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Operands {

	public static void main(String[] args) {
		Operands me = new Operands(BigDecimal.valueOf(1), BigDecimal.valueOf(2));
		// TODO 足し算をさせてみてください。
	}

	private final BigDecimal left;
	private final BigDecimal right;

	public BigDecimal execute(BinaryOperator<BigDecimal> action) {
		return action.apply(left, right);
	}

	public BigDecimal executeLeft(UnaryOperator<BigDecimal> action) {
		return action.apply(left);
	}

	public BigDecimal executeRight(UnaryOperator<BigDecimal> action) {
		return action.apply(right);
	}
}
