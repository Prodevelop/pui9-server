package es.prodevelop.pui9.documents.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiDocumentModelExtensionPk extends ITableDto {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String EXTENSION_COLUMN = "extension";
	@PuiGenerated
	String EXTENSION_FIELD = "extension";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getExtension();

	@PuiGenerated
	void setExtension(String extension);
}
