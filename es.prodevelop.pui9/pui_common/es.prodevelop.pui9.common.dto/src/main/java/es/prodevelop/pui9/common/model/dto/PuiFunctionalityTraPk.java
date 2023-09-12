package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityTraPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiFunctionalityTraPk extends AbstractTableDto implements IPuiFunctionalityTraPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiFunctionalityTraPk.FUNCTIONALITY_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String functionality;
	@PuiGenerated
	@PuiField(columnname = IPuiFunctionalityTraPk.LANG_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String lang;

	@PuiGenerated
	public PuiFunctionalityTraPk() {
	}

	@PuiGenerated
	public PuiFunctionalityTraPk(String functionality, String lang) {
		this.functionality = functionality;
		this.lang = lang;
	}

	@PuiGenerated
	@Override
	public String getFunctionality() {
		return functionality;
	}

	@PuiGenerated
	@Override
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
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
	public PuiFunctionalityTraPk createPk() {
		PuiFunctionalityTraPk pk = new PuiFunctionalityTraPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
