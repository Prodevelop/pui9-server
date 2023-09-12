package es.prodevelop.pui9.docgen.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiDocgenAttribute extends IPuiDocgenAttributePk {
	@PuiGenerated
	String LABEL_COLUMN = "label";
	@PuiGenerated
	String LABEL_FIELD = "label";
	@PuiGenerated
	String VALUE_COLUMN = "value";
	@PuiGenerated
	String VALUE_FIELD = "value";

	@PuiGenerated
	String getLabel();

	@PuiGenerated
	void setLabel(String label);

	@PuiGenerated
	String getValue();

	@PuiGenerated
	void setValue(String value);
}
