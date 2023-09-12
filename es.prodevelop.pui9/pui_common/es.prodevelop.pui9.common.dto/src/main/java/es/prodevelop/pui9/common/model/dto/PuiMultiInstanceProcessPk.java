package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMultiInstanceProcessPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiMultiInstanceProcessPk extends AbstractTableDto implements IPuiMultiInstanceProcessPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiMultiInstanceProcessPk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String id;

	@PuiGenerated
	public PuiMultiInstanceProcessPk() {
	}

	@PuiGenerated
	public PuiMultiInstanceProcessPk(String id) {
		this.id = id;
	}

	@PuiGenerated
	@Override
	public String getId() {
		return id;
	}

	@PuiGenerated
	@Override
	public void setId(String id) {
		this.id = id;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiMultiInstanceProcessPk createPk() {
		PuiMultiInstanceProcessPk pk = new PuiMultiInstanceProcessPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}