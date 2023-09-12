package es.prodevelop.pui9.docgen.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiDocgenModel extends IPuiDocgenModelPk {
	@PuiGenerated
	String LABEL_COLUMN = "label";
	@PuiGenerated
	String LABEL_FIELD = "label";
	@PuiGenerated
	String IDENTITY_FIELDS_COLUMN = "identity_fields";
	@PuiGenerated
	String IDENTITY_FIELDS_FIELD = "identityfields";

	@PuiGenerated
	String getLabel();

	@PuiGenerated
	void setLabel(String label);

	@PuiGenerated
	String getIdentityfields();

	@PuiGenerated
	void setIdentityfields(String identityfields);
}
