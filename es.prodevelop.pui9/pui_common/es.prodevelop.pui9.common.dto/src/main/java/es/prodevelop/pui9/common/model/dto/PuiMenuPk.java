package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiMenuPk extends AbstractTableDto implements IPuiMenuPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiMenuPk.NODE_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer node;

	@PuiGenerated
	public PuiMenuPk() {
	}

	@PuiGenerated
	public PuiMenuPk(Integer node) {
		this.node = node;
	}

	@PuiGenerated
	@Override
	public Integer getNode() {
		return node;
	}

	@PuiGenerated
	@Override
	public void setNode(Integer node) {
		this.node = node;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiMenuPk createPk() {
		PuiMenuPk pk = new PuiMenuPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
