package jp.satomaru.java8study.util.launcher;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

public class StringParserTest {

	@Test
	public void test() {
		assertThat(StringParser.parse(Integer.class, null), nullValue());
		assertThat(StringParser.parse(String.class, " foo "), is(" foo "));
		assertThat(StringParser.parse(Boolean.class, " true "), is(true));
		assertThat(StringParser.parse(Boolean.class, " false "), is(false));
		assertThat(StringParser.parse(Byte.class, " 127 "), is((byte) 127));
		assertThat(StringParser.parse(Integer.class, " -1,234 "), is((int) -1234));
		assertThat(StringParser.parse(Long.class, " -1,234 "), is((long) -1234));
		assertThat(StringParser.parse(Float.class, " -1,234.5 "), is((float) -1234.5));
		assertThat(StringParser.parse(Double.class, " -1,234.5 "), is((double) -1234.5));
		assertThat(StringParser.parse(BigInteger.class, " -1,234 "), is(BigInteger.valueOf(-1234)));
		assertThat(StringParser.parse(BigDecimal.class, " -1,234.5 "), is(BigDecimal.valueOf(-12345, 1)));
		assertThat(StringParser.parse(LocalDate.class, " 98/1/2 "), is(LocalDate.of(1998, 1, 2)));
		assertThat(StringParser.parse(LocalDate.class, " 02-3-4 "), is(LocalDate.of(2002, 3, 4)));
		assertThat(StringParser.parse(LocalDate.class, " 2015.05.06 "), is(LocalDate.of(2015, 5, 6)));
		assertThat(StringParser.parse(LocalTime.class, " 1:2:3 "), is(LocalTime.of(1, 2, 3)));
		assertThat(StringParser.parse(LocalTime.class, " 12:30:45.1 "), is(LocalTime.of(12, 30, 45, 100_000_000)));
		assertThat(StringParser.parse(LocalTime.class, " 12:30:45.123456789 "), is(LocalTime.of(12, 30, 45, 123456789)));
		assertThat(StringParser.parse(LocalDateTime.class, " 98/1/2   3:4:5 "), is(LocalDateTime.of(1998, 1, 2, 3, 4, 5)));
		assertThat(StringParser.parse(LocalDateTime.class, " 2015-01-02T03:30:45.123456789 "), is(LocalDateTime.of(2015, 1, 2, 3, 30, 45, 123456789)));
	}
}
