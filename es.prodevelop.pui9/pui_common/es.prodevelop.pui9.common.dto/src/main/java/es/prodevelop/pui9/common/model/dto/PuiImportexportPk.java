package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiImportexportPk extends AbstractTableDto implements IPuiImportexportPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiImportexportPk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = true)
	private Integer id;

	@PuiGenerated
	public PuiImportexportPk() {
	}

	@PuiGenerated
	public PuiImportexportPk(Integer id) {
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
	public PuiImportexportPk createPk() {
		PuiImportexportPk pk = new PuiImportexportPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
