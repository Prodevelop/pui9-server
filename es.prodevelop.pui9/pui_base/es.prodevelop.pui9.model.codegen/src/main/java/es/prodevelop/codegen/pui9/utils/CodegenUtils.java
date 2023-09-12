package es.prodevelop.codegen.pui9.utils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CodegenUtils {

	/**
	 * Something like ThisIsAComponent
	 * 
	 * @param dbName
	 * @return
	 */
	public static String getJavaName(String dbName) {
		if (StringUtils.isEmpty(dbName)) {
			return dbName;
		}

		String specialChars = getSpecialChars(dbName);
		String regexDelimiters = getRegexDelimiters(specialChars);

		// split the string using the delimiters and create an upper case after
		// every split
		StringBuilder sbJava = new StringBuilder();
		if (!regexDelimiters.isEmpty()) {
			List<String> splits = Arrays.asList(dbName.split(regexDelimiters));
			for (Iterator<String> it = splits.iterator(); it.hasNext();) {
				sbJava.append(StringUtils.capitalize(it.next().toLowerCase()));
			}
		} else {
			sbJava.append(StringUtils.capitalize(dbName.toLowerCase()));
		}

		return sbJava.toString().replaceAll("^[0-9]+", "");
	}

	private static String getSpecialChars(String name) {
		return Normalizer.normalize(name, Normalizer.Form.NFD)
				// Normalize string: remove accents
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				// remove special characters
				.replaceAll("[a-zA-Z0-9]+", "");
	}

	private static String getRegexDelimiters(String specialChars) {
		StringBuilder sbSplits = new StringBuilder();
		for (int i = 0; i < specialChars.toCharArray().length; i++) {
			char c = specialChars.charAt(i);
			sbSplits.append("\\" + c + "");
			if (i != specialChars.toCharArray().length - 1) {
				sbSplits.append("|");
			}
		}
		return sbSplits.toString();
	}

	public static String convertColumnNameToJavaName(String dbName) {
		if (StringUtils.isEmpty(dbName)) {
			return dbName;
		}

		// Normalize the string: remove accents
		return dbName.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				// remove special characters
				.replaceAll("[^a-zA-Z0-9]+", "")
				// to lower case
				.toLowerCase();
	}

}
