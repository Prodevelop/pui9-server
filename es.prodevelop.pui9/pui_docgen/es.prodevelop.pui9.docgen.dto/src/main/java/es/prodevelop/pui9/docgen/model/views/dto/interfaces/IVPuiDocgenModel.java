package es.prodevelop.pui9.docgen.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiDocgenModel extends IViewDto {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String ENTITY_COLUMN = "entity";
	@PuiGenerated
	String ENTITY_FIELD = "entity";
	@PuiGenerated
	String LABEL_COLUMN = "label";
	@PuiGenerated
	String LABEL_FIELD = "label";
	@PuiGenerated
	String IDENTITY_FIELDS_COLUMN = "identity_fields";
	@PuiGenerated
	String IDENTITY_FIELDS_FIELD = "identityfields";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getEntity();

	@PuiGenerated
	void setEntity(String entity);

	@PuiGenerated
	String getLabel();

	@PuiGenerated
	void setLabel(String label);

	@PuiGenerated
	String getIdentityfields();

	@PuiGenerated
	void setIdentityfields(String identityfields);
}
