package es.prodevelop.pui9.notifications.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiUserFcmPk extends AbstractTableDto implements IPuiUserFcmPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiUserFcmPk.TOKEN_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 300, islang = false, isgeometry = false, issequence = false)
	private String token;

	@PuiGenerated
	public PuiUserFcmPk() {
	}

	@PuiGenerated
	public PuiUserFcmPk(String token) {
		this.token = token;
	}

	@PuiGenerated
	@Override
	public String getToken() {
		return token;
	}

	@PuiGenerated
	@Override
	public void setToken(String token) {
		this.token = token;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiUserFcmPk createPk() {
		PuiUserFcmPk pk = new PuiUserFcmPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
