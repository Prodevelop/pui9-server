package es.prodevelop.codegen.pui9.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CodeEditorValues {

	JSON("application/json"),

	CSS("css"),

	HTML("htmlmixed"),

	JAVASCRIPT("javascript");

	private String value;

	private CodeEditorValues(String value) {
		this.value = value;
	}

	public static String[] asArrayString() {
		List<String> list = Arrays.asList(values()).stream().map(Enum::name).collect(Collectors.toList());
		return list.toArray(new String[0]);
	}

	public String getValue() {
		return value;
	}

}
