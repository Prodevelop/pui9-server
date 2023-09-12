package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemTra;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_subsystem_tra")
public class PuiSubsystemTra extends PuiSubsystemTraPk implements IPuiSubsystemTra {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiSubsystemTra.LANG_STATUS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer langstatus = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiSubsystemTra.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String name;

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
	public String getName() {
		return name;
	}

	@PuiGenerated
	@Override
	public void setName(String name) {
		this.name = name;
	}
}
