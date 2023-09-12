package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSessionPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiSessionPk extends AbstractTableDto implements IPuiSessionPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiSessionPk.UUID_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String uuid;

	@PuiGenerated
	public PuiSessionPk() {
	}

	@PuiGenerated
	public PuiSessionPk(String uuid) {
		this.uuid = uuid;
	}

	@PuiGenerated
	@Override
	public String getUuid() {
		return uuid;
	}

	@PuiGenerated
	@Override
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiSessionPk createPk() {
		PuiSessionPk pk = new PuiSessionPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
