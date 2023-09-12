package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiSubsystemPk extends AbstractTableDto implements IPuiSubsystemPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiSubsystemPk.SUBSYSTEM_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 3, islang = false, isgeometry = false, issequence = false)
	private String subsystem;

	@PuiGenerated
	public PuiSubsystemPk() {
	}

	@PuiGenerated
	public PuiSubsystemPk(String subsystem) {
		this.subsystem = subsystem;
	}

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
	@SuppressWarnings("unchecked")
	public PuiSubsystemPk createPk() {
		PuiSubsystemPk pk = new PuiSubsystemPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
