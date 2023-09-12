package es.prodevelop.pui9.common.model.dto.interfaces;

import java.util.List;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiElasticsearchViews extends IPuiElasticsearchViewsPk {
	@PuiGenerated
	String IDENTITY_FIELDS_COLUMN = "identity_fields";
	@PuiGenerated
	String IDENTITY_FIELDS_FIELD = "identityfields";

	@PuiGenerated
	String getIdentityfields();

	@PuiGenerated
	void setIdentityfields(String identityfields);

	String getParsedViewName();

	List<String> getParsedIdFields();

}
