package es.prodevelop.pui9.dashboards.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboard;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_dashboard")
public class PuiDashboard extends PuiDashboardPk implements IPuiDashboard {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDashboard.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IPuiDashboard.DEFINITION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String definition;

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
	public String getDefinition() {
		return definition;
	}

	@PuiGenerated
	@Override
	public void setDefinition(String definition) {
		this.definition = definition;
	}
}