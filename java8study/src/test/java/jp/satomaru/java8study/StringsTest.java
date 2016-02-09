package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class StringsTest {

	@Test
	public void testCreateMap() {

		List<String> keyValueList = new ArrayList<>();
		keyValueList.add("key1:value1");
		keyValueList.add("key2:value2");
		keyValueList.add(null);
		keyValueList.add("key3:value3");
		keyValueList.add("key4:value4");
		keyValueList.add(null);
		keyValueList.add("key5:value5");

		Map<String, String> actualMap = Strings.createMap(keyValueList);

		assertThat(actualMap, hasEntry("key1", "value1"));
		assertThat(actualMap, hasEntry("key2", "value2"));
		assertThat(actualMap, hasEntry("key3", "value3"));
		assertThat(actualMap, hasEntry("key4", "value4"));
		assertThat(actualMap, hasEntry("key5", "value5"));
	}

}
