package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiDocgenTemplatePk extends AbstractTableDto implements IPuiDocgenTemplatePk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplatePk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = true)
	private Integer id;

	@PuiGenerated
	public PuiDocgenTemplatePk() {
	}

	@PuiGenerated
	public PuiDocgenTemplatePk(Integer id) {
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
	public PuiDocgenTemplatePk createPk() {
		PuiDocgenTemplatePk pk = new PuiDocgenTemplatePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
