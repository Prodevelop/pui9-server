package es.prodevelop.pui9.alerts.model.dto;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertPk;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiAlertPk extends AbstractTableDto implements IPuiAlertPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertPk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = true)
	private Integer id;

	@PuiGenerated
	public PuiAlertPk() {
	}

	@PuiGenerated
	public PuiAlertPk(Integer id) {
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
	public PuiAlertPk createPk() {
		PuiAlertPk pk = new PuiAlertPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
