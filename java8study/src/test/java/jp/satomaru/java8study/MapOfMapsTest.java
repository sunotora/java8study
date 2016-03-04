package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapOfMapsTest {

	@Test
	public void test() throws Exception {



		Map<String, Map<String, BigDecimal>> actual = MapOfMaps.<String, String, BigDecimal>begin()
			.begin("James")
				.add("Jun", BigDecimal.valueOf(100000))
				.add("Feb", BigDecimal.valueOf(150000))
			.end()
			.begin("Debit")
				.add("Jun", BigDecimal.valueOf(200000))
				.add("Feb", BigDecimal.valueOf(250000))
			.end()
			.begin("Bob")
				.add("Jun", BigDecimal.valueOf(300000))
				.add("Feb", BigDecimal.valueOf(350000))
			.end()
		.end(true);


		assertThat(actual.size(), is(3));

		assertThat(actual, hasEntry("James", getJamesSalary()));
		assertThat(actual, hasEntry("Bob", getBobSalary()));

		try {
			actual.put("hoge", getJamesSalary());
			fail();
		} catch (UnsupportedOperationException e) {}

		try {
			actual.get("Debit").put("Mar", BigDecimal.valueOf(500000));
			fail();
		} catch (UnsupportedOperationException e) {}
	}

	private Map<String, BigDecimal> getJamesSalary () {
		Map<String, BigDecimal> map = new HashMap<>();
		map.put("Jun", BigDecimal.valueOf(100000));
		map.put("Feb", BigDecimal.valueOf(150000));
		return map;
	}

	private Map<String, BigDecimal> getBobSalary () {
		Map<String, BigDecimal> map = new HashMap<>();
		map.put("Jun", BigDecimal.valueOf(300000));
		map.put("Feb", BigDecimal.valueOf(350000));
		return map;
	}
}
