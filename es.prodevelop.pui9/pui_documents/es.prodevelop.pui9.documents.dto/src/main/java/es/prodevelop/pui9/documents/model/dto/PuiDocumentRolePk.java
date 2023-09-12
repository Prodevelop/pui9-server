package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRolePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiDocumentRolePk extends AbstractTableDto implements IPuiDocumentRolePk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentRolePk.ROLE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String role;

	@PuiGenerated
	public PuiDocumentRolePk() {
	}

	@PuiGenerated
	public PuiDocumentRolePk(String role) {
		this.role = role;
	}

	@PuiGenerated
	@Override
	public String getRole() {
		return role;
	}

	@PuiGenerated
	@Override
	public void setRole(String role) {
		this.role = role;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiDocumentRolePk createPk() {
		PuiDocumentRolePk pk = new PuiDocumentRolePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
