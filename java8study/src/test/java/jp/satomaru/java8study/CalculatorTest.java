package jp.satomaru.java8study;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class CalculatorTest {

	private static Calculator cal = new Calculator();

	private static BigDecimal ZERO = BigDecimal.ZERO;
	private static BigDecimal ONE = BigDecimal.ONE;
	private static BigDecimal TWO = BigDecimal.valueOf(2);
	private static BigDecimal THREE = BigDecimal.valueOf(3);
	private static BigDecimal FOUR = BigDecimal.valueOf(4);
	private static BigDecimal MINUS_ONE = BigDecimal.valueOf(-1);

	@Test
	public void add0_1() {
		assertThat(cal.execute("add", cal.itemsBuilder(ZERO, ONE)), equalTo(ONE));
	}

	@Test
	public void add1_2() {
		assertThat(cal.execute("add", cal.itemsBuilder(ONE, TWO)), equalTo(THREE));
	}

	@Test
	public void sub2_1() {
		assertThat(cal.execute("sub", cal.itemsBuilder(TWO, ONE)), equalTo(ONE));
	}

	@Test
	public void sub1_2() {
		assertThat(cal.execute("sub", cal.itemsBuilder(ONE, TWO)), equalTo(MINUS_ONE));
	}

	@Test
	public void mul1_2() {
		assertThat(cal.execute("mul", cal.itemsBuilder(ONE, TWO)), equalTo(TWO));
	}

	@Test
	public void mul2_2() {
		assertThat(cal.execute("mul", cal.itemsBuilder(TWO, TWO)), equalTo(FOUR));
	}

	@Test
	public void div2_1() {
		assertThat(cal.execute("div", cal.itemsBuilder(TWO, ONE )), equalTo(TWO));
	}

	@Test
	public void div2_2() {
		assertThat(cal.execute("div", cal.itemsBuilder(TWO, TWO)), equalTo(ONE));
	}
}
