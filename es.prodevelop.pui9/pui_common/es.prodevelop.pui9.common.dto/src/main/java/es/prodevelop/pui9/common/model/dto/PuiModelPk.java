package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiModelPk extends AbstractTableDto implements IPuiModelPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiModelPk.MODEL_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;

	@PuiGenerated
	public PuiModelPk() {
	}

	@PuiGenerated
	public PuiModelPk(String model) {
		this.model = model;
	}

	@PuiGenerated
	@Override
	public String getModel() {
		return model;
	}

	@PuiGenerated
	@Override
	public void setModel(String model) {
		this.model = model;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiModelPk createPk() {
		PuiModelPk pk = new PuiModelPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
