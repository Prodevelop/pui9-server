package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguage;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_language")
public class PuiLanguage extends PuiLanguagePk implements IPuiLanguage {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiLanguage.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IPuiLanguage.ISDEFAULT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer isdefault = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiLanguage.ENABLED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer enabled = 1;

	@PuiGenerated
	@Override
	public String getName() {
		return name;
	}

	@PuiGenerated
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@PuiGenerated
	@Override
	public Integer getIsdefault() {
		return isdefault;
	}

	@PuiGenerated
	@Override
	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}

	@PuiGenerated
	@Override
	public Integer getEnabled() {
		return enabled;
	}

	@PuiGenerated
	@Override
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
}
