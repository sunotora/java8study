package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class UtilsTest {

	private static List<String> keyValueList;
	private static Map<String, String> map;
	private static List<Object> multiObjectList;
	private static Set<String> classSet;

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

	@BeforeClass
	public static void createClassSet() {
		classSet = new HashSet<>();
		classSet.add("lombok.experimental.Accessors");
		classSet.add("lombok.Singular");
		classSet.add("lombok.extern.log4j.Log4j");
		classSet.add("lombok.Cleanup");
		classSet.add("lombok.RequiredArgsConstructor$AnyAnnotation");
		classSet.add("lombok.experimental.Helper");
		classSet.add("lombok.launch.PatchFixesHider$ValPortal");
		classSet.add("lombok.experimental.ExtensionMethod");
		classSet.add("lombok.NonNull");
		classSet.add("lombok.AllArgsConstructor$AnyAnnotation");
		classSet.add("lombok.Getter$AnyAnnotation");
		classSet.add("lombok.extern.slf4j.Slf4j");
		classSet.add("lombok.Builder$ObtainVia");
		classSet.add("lombok.experimental.Tolerate");
		classSet.add("lombok.Delegate");
		classSet.add("lombok.extern.slf4j.XSlf4j");
		classSet.add("lombok.experimental.UtilityClass");
		classSet.add("lombok.AccessLevel");
		classSet.add("lombok.launch.PatchFixesHider$Util");
		classSet.add("lombok.experimental.Delegate");
		classSet.add("lombok.delombok.ant.Tasks$Format");
		classSet.add("lombok.val");
		classSet.add("lombok.extern.java.Log");
		classSet.add("lombok.delombok.ant.Tasks$Delombok");
		classSet.add("lombok.launch.Agent");
		classSet.add("lombok.NoArgsConstructor");
		classSet.add("lombok.ConfigurationKeys$8");
		classSet.add("lombok.ConfigurationKeys$9");
		classSet.add("lombok.ConfigurationKeys$6");
		classSet.add("lombok.extern.apachecommons.CommonsLog");
		classSet.add("lombok.ConfigurationKeys$7");
		classSet.add("lombok.SneakyThrows");
		classSet.add("lombok.ConfigurationKeys$4");
		classSet.add("lombok.ConfigurationKeys$5");
		classSet.add("lombok.RequiredArgsConstructor");
		classSet.add("lombok.experimental.Value");
		classSet.add("lombok.experimental.Wither$AnyAnnotation");
		classSet.add("lombok.experimental.Wither");
		classSet.add("lombok.launch.PatchFixesHider$Delegate");
		classSet.add("lombok.launch.AnnotationProcessorHider$AnnotationProcessor");
		classSet.add("lombok.ConfigurationKeys$19");
		classSet.add("lombok.ConfigurationKeys$16");
		classSet.add("lombok.ConfigurationKeys$15");
		classSet.add("lombok.Generated");
		classSet.add("lombok.ConfigurationKeys$18");
		classSet.add("lombok.ConfigurationKeys$17");
		classSet.add("lombok.ConfigurationKeys$12");
		classSet.add("lombok.ToString");
		classSet.add("lombok.ConfigurationKeys$11");
		classSet.add("lombok.ConfigurationKeys$14");
		classSet.add("lombok.ConfigurationKeys$13");
		classSet.add("lombok.ConfigurationKeys$10");
		classSet.add("lombok.Synchronized");
		classSet.add("lombok.EqualsAndHashCode");
		classSet.add("lombok.launch.ShadowClassLoader");
		classSet.add("lombok.NoArgsConstructor$AnyAnnotation");
		classSet.add("lombok.Builder");
		classSet.add("lombok.ConfigurationKeys$27");
		classSet.add("lombok.ConfigurationKeys$26");
		classSet.add("lombok.launch.PatchFixesHider$Transform");
		classSet.add("lombok.ConfigurationKeys$29");
		classSet.add("lombok.ConfigurationKeys$28");
		classSet.add("lombok.ConfigurationKeys$23");
		classSet.add("lombok.ConfigurationKeys$22");
		classSet.add("lombok.ConfigurationKeys$25");
		classSet.add("lombok.ConfigurationKeys$24");
		classSet.add("lombok.Lombok");
		classSet.add("lombok.ConfigurationKeys");
		classSet.add("lombok.delombok.ant.Tasks");
		classSet.add("lombok.ConfigurationKeys$21");
		classSet.add("lombok.ConfigurationKeys$20");
		classSet.add("lombok.experimental.NonFinal");
		classSet.add("lombok.experimental.FieldDefaults");
		classSet.add("lombok.launch.PatchFixesHider$Val");
		classSet.add("lombok.Setter");
		classSet.add("lombok.Getter");
		classSet.add("lombok.launch.PatchFixesHider$PatchFixes");
		classSet.add("lombok.Value");
		classSet.add("lombok.launch.Main");
		classSet.add("lombok.ConfigurationKeys$38");
		classSet.add("lombok.Setter$AnyAnnotation");
		classSet.add("lombok.ConfigurationKeys$37");
		classSet.add("lombok.ConfigurationKeys$39");
		classSet.add("lombok.experimental.Builder");
		classSet.add("lombok.launch.AnnotationProcessorHider");
		classSet.add("lombok.ConfigurationKeys$34");
		classSet.add("lombok.ConfigurationKeys$33");
		classSet.add("lombok.ConfigurationKeys$36");
		classSet.add("lombok.launch.PatchFixesHider$ExtensionMethod");
		classSet.add("lombok.ConfigurationKeys$35");
		classSet.add("lombok.ConfigurationKeys$30");
		classSet.add("lombok.ConfigurationKeys$32");
		classSet.add("lombok.ConfigurationKeys$31");
		classSet.add("lombok.EqualsAndHashCode$AnyAnnotation");
		classSet.add("lombok.launch.PatchFixesHider");
		classSet.add("lombok.ConfigurationKeys$48");
		classSet.add("lombok.experimental.PackagePrivate");
		classSet.add("lombok.ConfigurationKeys$2");
		classSet.add("lombok.ConfigurationKeys$45");
		classSet.add("lombok.Data");
		classSet.add("lombok.extern.log4j.Log4j2");
		classSet.add("lombok.ConfigurationKeys$3");
		classSet.add("lombok.ConfigurationKeys$44");
		classSet.add("lombok.ConfigurationKeys$47");
		classSet.add("lombok.ConfigurationKeys$1");
		classSet.add("lombok.ConfigurationKeys$46");
		classSet.add("lombok.AllArgsConstructor");
		classSet.add("lombok.ConfigurationKeys$41");
		classSet.add("lombok.ConfigurationKeys$40");
		classSet.add("lombok.ConfigurationKeys$43");
		classSet.add("lombok.ConfigurationKeys$42");
		classSet.add("lombok.launch.PatchFixesHider$LombokDeps");
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

	@Test
	public void testLookForRecentDayOf() throws Exception {

		assertThat(Utils.lookForRecentDayOf(15, DayOfWeek.MONDAY),    is(LocalDate.of(2016,  2, 15)));
		assertThat(Utils.lookForRecentDayOf(16, DayOfWeek.TUESDAY),   is(LocalDate.of(2015,  6, 16)));
		assertThat(Utils.lookForRecentDayOf(13, DayOfWeek.WEDNESDAY), is(LocalDate.of(2016,  1, 13)));
		assertThat(Utils.lookForRecentDayOf(24, DayOfWeek.WEDNESDAY), is(LocalDate.of(2015,  6, 24)));
		assertThat(Utils.lookForRecentDayOf(31, DayOfWeek.WEDNESDAY), is(LocalDate.of(2014, 12, 31)));

//		テストケース確認用
//		for (int i = 1 ; i < 32 ; i++) {
//			for (DayOfWeek week : DayOfWeek.values()) {
//				System.out.println(Utils.lookForRecentDayOf(i, week) + " : " + week);
//			}
//		}
	}

	private final String mavenRep = "【mavenリポジトリ】"; // 自環境のものに変更すること

	@Test
	public void testSearchClassInJarFromLombok() throws Exception {

		Set<String> actualSet = Utils.searchClassInJar(new File(mavenRep, "lombok-1.16.6.jar"), "lombok");

		for (String str : classSet) {
			assertTrue(actualSet.contains(str));
		}

	}

	@Test
	public void testSearchClassNotInJarFromLombok() throws Exception {

		Set<String> actualSet = Utils.searchClassInJar(new File(mavenRep, "lombok-1.16.6.jar"), "lombok");

		assertFalse(actualSet.contains("org.objectweb.asm"));
		assertFalse(actualSet.contains("Class50.lombok.eclipse.agent"));

	}

	@Test(expected=FileNotFoundException.class)
	public void testSearchClassInJarNotFoundDirectory() throws Exception {

		Utils.searchClassInJar(new File(mavenRep + "dummy", "lombok-1.16.6.jar"), "lombok");

	}

	@Test(expected=FileNotFoundException.class)
	public void testSearchClassInJarNotFoundFile() throws Exception {

		Utils.searchClassInJar(new File(mavenRep, "lombok-1.16.6.jar_dummy"), "lombok");

	}

	@Test
	public void testSearchClassNotInJarNoPackage() throws Exception {

		Set<String> actualSet = Utils.searchClassInJar(new File(mavenRep, "lombok-1.16.6.jar"), "lombok_dummy");

		assertThat(actualSet, empty());
	}

}
