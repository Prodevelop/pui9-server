package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModelPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiDocgenModelPk extends AbstractTableDto implements IPuiDocgenModelPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenModelPk.MODEL_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;

	@PuiGenerated
	public PuiDocgenModelPk() {
	}

	@PuiGenerated
	public PuiDocgenModelPk(String model) {
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
	public PuiDocgenModelPk createPk() {
		PuiDocgenModelPk pk = new PuiDocgenModelPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
