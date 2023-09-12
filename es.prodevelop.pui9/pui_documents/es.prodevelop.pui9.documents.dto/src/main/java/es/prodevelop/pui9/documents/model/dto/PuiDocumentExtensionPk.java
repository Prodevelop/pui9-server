package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtensionPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiDocumentExtensionPk extends AbstractTableDto implements IPuiDocumentExtensionPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentExtensionPk.EXTENSION_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	private String extension;

	@PuiGenerated
	public PuiDocumentExtensionPk() {
	}

	@PuiGenerated
	public PuiDocumentExtensionPk(String extension) {
		this.extension = extension;
	}

	@PuiGenerated
	@Override
	public String getExtension() {
		return extension;
	}

	@PuiGenerated
	@Override
	public void setExtension(String extension) {
		this.extension = extension;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiDocumentExtensionPk createPk() {
		PuiDocumentExtensionPk pk = new PuiDocumentExtensionPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
