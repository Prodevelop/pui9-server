package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguagePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiLanguagePk extends AbstractTableDto implements IPuiLanguagePk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiLanguagePk.ISOCODE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String isocode;

	@PuiGenerated
	public PuiLanguagePk() {
	}

	@PuiGenerated
	public PuiLanguagePk(String isocode) {
		this.isocode = isocode;
	}

	@PuiGenerated
	@Override
	public String getIsocode() {
		return isocode;
	}

	@PuiGenerated
	@Override
	public void setIsocode(String isocode) {
		this.isocode = isocode;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiLanguagePk createPk() {
		PuiLanguagePk pk = new PuiLanguagePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
