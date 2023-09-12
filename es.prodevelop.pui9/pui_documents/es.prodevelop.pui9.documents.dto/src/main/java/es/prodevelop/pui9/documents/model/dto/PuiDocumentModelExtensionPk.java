package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtensionPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiDocumentModelExtensionPk extends AbstractTableDto implements IPuiDocumentModelExtensionPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentModelExtensionPk.MODEL_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentModelExtensionPk.EXTENSION_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	private String extension;

	@PuiGenerated
	public PuiDocumentModelExtensionPk() {
	}

	@PuiGenerated
	public PuiDocumentModelExtensionPk(String extension, String model) {
		this.extension = extension;
		this.model = model;
	}

	@PuiGenerated
	@Override
	public String getModel() {
		return model;
	}

	@PuiGenerated
	@Override
	public void setModel(String model) {
		this.model = model;
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
	public PuiDocumentModelExtensionPk createPk() {
		PuiDocumentModelExtensionPk pk = new PuiDocumentModelExtensionPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
