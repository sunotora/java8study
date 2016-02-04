package jp.satomaru.java8study;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Operands {

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

	public static void main(String[] args) {
		Operands me = new Operands(BigDecimal.valueOf(1), BigDecimal.valueOf(2));
		// 足し算をさせてみてください。
		System.out.println(me.execute(Operands::add));
		// R apply(T t, U u);
	}

	private static BigDecimal add(BigDecimal left, BigDecimal right) {
		return left.add(right);
	}
}
