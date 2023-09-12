package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKeyPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiApiKeyPk extends AbstractTableDto implements IPuiApiKeyPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiApiKeyPk.API_KEY_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String apikey;

	@PuiGenerated
	public PuiApiKeyPk() {
	}

	@PuiGenerated
	public PuiApiKeyPk(String apikey) {
		this.apikey = apikey;
	}

	@PuiGenerated
	@Override
	public String getApikey() {
		return apikey;
	}

	@PuiGenerated
	@Override
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiApiKeyPk createPk() {
		PuiApiKeyPk pk = new PuiApiKeyPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
