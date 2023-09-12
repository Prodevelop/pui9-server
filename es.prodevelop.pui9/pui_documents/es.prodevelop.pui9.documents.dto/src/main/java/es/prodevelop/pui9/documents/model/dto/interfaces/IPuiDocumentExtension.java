package es.prodevelop.pui9.documents.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiDocumentExtension extends IPuiDocumentExtensionPk {
	@PuiGenerated
	String MAX_SIZE_COLUMN = "max_size";
	@PuiGenerated
	String MAX_SIZE_FIELD = "maxsize";

	@PuiGenerated
	Integer getMaxsize();

	@PuiGenerated
	void setMaxsize(Integer maxsize);
}
