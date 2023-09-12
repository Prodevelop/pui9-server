package es.prodevelop.pui9.docgen.dto;

import es.prodevelop.pui9.docgen.enums.DocgenMappingOriginEnum;
import es.prodevelop.pui9.utils.IPuiObject;

public class DocgenMapping implements IPuiObject {
	private static final long serialVersionUID = 1L;

	private String field;
	private String tag;
	private DocgenMappingOriginEnum origin = DocgenMappingOriginEnum.V;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public DocgenMappingOriginEnum getOrigin() {
		return origin;
	}

	public void setOrigin(DocgenMappingOriginEnum origin) {
		this.origin = origin;
	}

	@Override
	public String toString() {
		return tag + "::" + field + " (" + (origin != null ? origin : "null") + ")";
	}

}
