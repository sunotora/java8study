package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.junit.Before;
import org.junit.Test;

public class CsvBuilderTest {

	@Data
	@RequiredArgsConstructor
	@AllArgsConstructor
	public class MyEntity {
		private final int id;
		private String name;
		private LocalDate since;
		private boolean bool;
	}

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private CsvBuilder<MyEntity> tested;

	@Before
	public void setUp() {
		tested = new CsvBuilder<>(entity -> new Object[] {entity.id, entity.name, entity.since, entity.bool});
		tested.addFormatter(LocalDate.class, date -> date.format(DateTimeFormatter.ofPattern("\"yyyy-MM-dd\"")));
	}

	@Test
	public void testMyEntityToCsv() throws Exception {
		String actual = tested.build(Arrays.asList(
				new MyEntity(1, "foo", LocalDate.of(2015, 12, 1), true),
				new MyEntity(2, "bar", LocalDate.of(2016, 2, 15), false),
				new MyEntity(3, "baz", null, false)));

		assertThat(actual, is(
				"1,\"foo\",\"2015-12-01\",true" + LINE_SEPARATOR +
				"2,\"bar\",\"2016-02-15\",false" + LINE_SEPARATOR +
				"3,\"baz\",,false"));
	}
}
