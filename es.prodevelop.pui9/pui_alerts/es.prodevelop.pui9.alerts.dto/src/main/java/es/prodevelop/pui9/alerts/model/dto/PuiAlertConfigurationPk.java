package es.prodevelop.pui9.alerts.model.dto;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfigurationPk;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiAlertConfigurationPk extends AbstractTableDto implements IPuiAlertConfigurationPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfigurationPk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = true)
	private Integer id;

	@PuiGenerated
	public PuiAlertConfigurationPk() {
	}

	@PuiGenerated
	public PuiAlertConfigurationPk(Integer id) {
		this.id = id;
	}

	@PuiGenerated
	@Override
	public Integer getId() {
		return id;
	}

	@PuiGenerated
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiAlertConfigurationPk createPk() {
		PuiAlertConfigurationPk pk = new PuiAlertConfigurationPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
