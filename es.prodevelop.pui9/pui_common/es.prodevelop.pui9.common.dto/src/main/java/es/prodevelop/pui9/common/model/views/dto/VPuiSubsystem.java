package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiSubsystem;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_subsystem")
public class VPuiSubsystem extends AbstractViewDto implements IVPuiSubsystem {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiSubsystem.SUBSYSTEM_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 3, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String subsystem;
	@PuiGenerated
	@PuiField(columnname = IVPuiSubsystem.NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IVPuiSubsystem.LANG_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.completelyhidden)
	private String lang;

	@PuiGenerated
	@Override
	public String getSubsystem() {
		return subsystem;
	}

	@PuiGenerated
	@Override
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
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
}
