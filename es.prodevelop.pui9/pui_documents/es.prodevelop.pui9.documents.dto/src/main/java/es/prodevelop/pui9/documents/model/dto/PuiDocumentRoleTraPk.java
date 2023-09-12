package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRoleTraPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiDocumentRoleTraPk extends AbstractTableDto implements IPuiDocumentRoleTraPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentRoleTraPk.ROLE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String role;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentRoleTraPk.LANG_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String lang;

	@PuiGenerated
	public PuiDocumentRoleTraPk() {
	}

	@PuiGenerated
	public PuiDocumentRoleTraPk(String lang, String role) {
		this.lang = lang;
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
	public String getLang() {
		return lang;
	}

	@PuiGenerated
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiDocumentRoleTraPk createPk() {
		PuiDocumentRoleTraPk pk = new PuiDocumentRoleTraPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
