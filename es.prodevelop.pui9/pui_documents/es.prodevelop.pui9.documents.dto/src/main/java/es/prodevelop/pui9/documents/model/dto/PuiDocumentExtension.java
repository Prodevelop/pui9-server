package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtension;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_document_extension")
public class PuiDocumentExtension extends PuiDocumentExtensionPk implements IPuiDocumentExtension {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentExtension.MAX_SIZE_COLUMN, ispk = false, nullable = true, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer maxsize;

	@PuiGenerated
	@Override
	public Integer getMaxsize() {
		return maxsize;
	}

	@PuiGenerated
	@Override
	public void setMaxsize(Integer maxsize) {
		this.maxsize = maxsize;
	}
}
