package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class UtilsTest {

	private static List<String> keyValueList;
	private static Map<String, String> map;
	private static List<Object> multiObjectList;

	@BeforeClass
	public static void createKeyValueList() {
		keyValueList = new ArrayList<>();
		keyValueList.add("key1:value1");
		keyValueList.add("key2:value2");
		keyValueList.add(null);
		keyValueList.add("key3:value3");
		keyValueList.add("key4:value4");
		keyValueList.add(null);
		keyValueList.add("key5:value5");
	}

	@BeforeClass
	public static void createMap() {
		map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		map.put("key4", "value4");
		map.put("key5", "value5");
		map.put("key6", null);
	}

	@BeforeClass
	public static void createMultiObjectList() {
		multiObjectList = new ArrayList<>();
		multiObjectList.add(Integer.valueOf(5));
		multiObjectList.add(Long.valueOf(1000));
		multiObjectList.add(new Object());
		multiObjectList.add(BigDecimal.valueOf(50));
		multiObjectList.add("str1");
		multiObjectList.add(BigDecimal.valueOf(100));
		multiObjectList.add(Double.valueOf(10000));
		multiObjectList.add(Integer.valueOf(10));
		multiObjectList.add("str2");
	}


	@Test
	public void testToMap() {

		Map<String, String> actualMap = Utils.toMap(keyValueList);

		assertThat(actualMap, hasEntry("key1", "value1"));
		assertThat(actualMap, hasEntry("key2", "value2"));
		assertThat(actualMap, hasEntry("key3", "value3"));
		assertThat(actualMap, hasEntry("key4", "value4"));
		assertThat(actualMap, hasEntry("key5", "value5"));
	}

	@Test
	public void testToStringList() throws Exception {

		List<String> actualList = Utils.toStringList(map);

		String[] expected = {"key1:value1", "key2:value2","key3:value3","key4:value4","key5:value5", "key6:"};

		assertThat(actualList, is(containsInAnyOrder(expected)));
	}

	@Test
	public void testGroupByType() throws Exception {

		Map<Class<?>, List<Object>> actualMap = Utils.groupByType(multiObjectList);

		List<Object> actualList1 = actualMap.get(String.class);
		Object[] expected1 = {"str1", "str2"};
		assertThat(actualList1, is(containsInAnyOrder(expected1)));

		List<Object> actualList2 = actualMap.get(BigDecimal.class);
		Object[] expected2 = {BigDecimal.valueOf(50), BigDecimal.valueOf(100)};
		assertThat(actualList2, is(containsInAnyOrder(expected2)));

		List<Object> actualList3 = actualMap.get(Long.class);
		Object[] expected3 = {Long.valueOf(1000)};
		assertThat(actualList3, is(containsInAnyOrder(expected3)));
	}
}
