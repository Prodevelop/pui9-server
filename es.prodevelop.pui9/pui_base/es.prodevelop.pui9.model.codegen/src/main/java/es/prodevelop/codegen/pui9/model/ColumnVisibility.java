package es.prodevelop.codegen.pui9.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ColumnVisibility {

	visible,

	hidden,

	completelyhidden;

	public static String[] asArrayString() {
		List<String> list = Arrays.asList(values()).stream().map(Enum::name).collect(Collectors.toList());
		return list.toArray(new String[0]);
	}

}
