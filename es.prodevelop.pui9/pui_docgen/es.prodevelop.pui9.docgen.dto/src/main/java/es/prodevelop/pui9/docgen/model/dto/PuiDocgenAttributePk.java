package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttributePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiDocgenAttributePk extends AbstractTableDto implements IPuiDocgenAttributePk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenAttributePk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String id;

	@PuiGenerated
	public PuiDocgenAttributePk() {
	}

	@PuiGenerated
	public PuiDocgenAttributePk(String id) {
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
	public PuiDocgenAttributePk createPk() {
		PuiDocgenAttributePk pk = new PuiDocgenAttributePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
