package es.prodevelop.pui9.docgen.enums;

public enum DocgenMappingOriginEnum {

	V("View"),

	S("System"),

	T("Table"),

	U("User");

	public final String description;

	private DocgenMappingOriginEnum(String description) {
		this.description = description;
	}

}
