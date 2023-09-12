package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRole;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_document_role", tabletranslationname = "pui_document_role_tra")
public class PuiDocumentRole extends PuiDocumentRolePk implements IPuiDocumentRole {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentRole.LANG_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = true, isgeometry = false, issequence = false)
	private String lang;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentRole.LANG_STATUS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = true, isgeometry = false, issequence = false)
	private Integer langstatus = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiDocumentRole.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = true, isgeometry = false, issequence = false)
	private String description;

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
	public Integer getLangstatus() {
		return langstatus;
	}

	@PuiGenerated
	@Override
	public void setLangstatus(Integer langstatus) {
		this.langstatus = langstatus;
	}

	@PuiGenerated
	@Override
	public String getDescription() {
		return description;
	}

	@PuiGenerated
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
