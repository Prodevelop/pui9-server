package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiProfilePk extends AbstractTableDto implements IPuiProfilePk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiProfilePk.PROFILE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String profile;

	@PuiGenerated
	public PuiProfilePk() {
	}

	@PuiGenerated
	public PuiProfilePk(String profile) {
		this.profile = profile;
	}

	@PuiGenerated
	@Override
	public String getProfile() {
		return profile;
	}

	@PuiGenerated
	@Override
	public void setProfile(String profile) {
		this.profile = profile;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiProfilePk createPk() {
		PuiProfilePk pk = new PuiProfilePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
