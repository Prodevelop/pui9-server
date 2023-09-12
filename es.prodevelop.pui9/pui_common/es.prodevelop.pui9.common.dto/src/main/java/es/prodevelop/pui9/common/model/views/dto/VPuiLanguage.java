package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_language")
public class VPuiLanguage extends AbstractViewDto implements IVPuiLanguage {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiLanguage.ISOCODE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String isocode;
	@PuiGenerated
	@PuiField(columnname = IVPuiLanguage.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IVPuiLanguage.ISDEFAULT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private Integer isdefault;
	@PuiGenerated
	@PuiField(columnname = IVPuiLanguage.ENABLED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private Integer enabled;

	@PuiGenerated
	@Override
	public String getIsocode() {
		return isocode;
	}

	@PuiGenerated
	@Override
	public void setIsocode(String isocode) {
		this.isocode = isocode;
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
