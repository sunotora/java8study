package jp.satomaru.java8study.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {

	public static String rightSuppress(String string, String suppressed) {
		String result = string;

		while (result.endsWith(suppressed)) {
			if (result.equals(suppressed)) {
				return "";
			}

			result = result.substring(0, result.length() - suppressed.length());
		}

		return result;
	}

	public static String leftSuppress(String string, String suppressed) {
		String result = string;

		while (result.startsWith(suppressed)) {
			if (result.equals(suppressed)) {
				return "";
			}

			result = result.substring(suppressed.length());
		}

		return result;
	}

	public static String bothSuppress(String string, String suppressed) {
		return rightSuppress(leftSuppress(string, suppressed), suppressed);
	}
}
