package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ListMapsTest {

	@Test
	public void test() throws Exception {
		Map<String, List<Integer>> actual = ListMaps.<String, Integer>begin()
			.begin("foo").add(11, 12).end()
			.begin("bar").add(21, 22).end()
		.end(true);

		assertThat(actual.size(), is(2));
		assertThat(actual, hasEntry("foo", Arrays.asList(11, 12)));
		assertThat(actual, hasEntry("bar", Arrays.asList(21, 22)));

		try {
			actual.put("baz", new ArrayList<>());
			fail();
		} catch (UnsupportedOperationException e) {}

		try {
			actual.get("bar").add(23);
			fail();
		} catch (UnsupportedOperationException e) {}
	}
}
