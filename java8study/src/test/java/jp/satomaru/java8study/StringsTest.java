package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class StringsTest {

	private static List<String> keyValueList;
	private static Map<String, String> map;

	@BeforeClass
	public static void beforeClass() {
		keyValueList = new ArrayList<>();
		keyValueList.add("key1:value1");
		keyValueList.add("key2:value2");
		keyValueList.add(null);
		keyValueList.add("key3:value3");
		keyValueList.add("key4:value4");
		keyValueList.add(null);
		keyValueList.add("key5:value5");

		map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		map.put("key4", "value4");
		map.put("key5", "value5");
		map.put("key6", null);
	}

	@Test
	public void testCreateMap() {

		Map<String, String> actualMap = Strings.createMap(keyValueList);

		assertThat(actualMap, hasEntry("key1", "value1"));
		assertThat(actualMap, hasEntry("key2", "value2"));
		assertThat(actualMap, hasEntry("key3", "value3"));
		assertThat(actualMap, hasEntry("key4", "value4"));
		assertThat(actualMap, hasEntry("key5", "value5"));
	}

	@Test
	public void testMapToStringList() throws Exception {
		List<String> actualList = Strings.mapToStringList(map);

		String[] expected = {"key1:value1", "key2:value2","key3:value3","key4:value4","key5:value5", "key6:"};

		assertThat(actualList, is(containsInAnyOrder(expected)));
	}
}
