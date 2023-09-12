package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfilePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiUserProfilePk extends AbstractTableDto implements IPuiUserProfilePk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiUserProfilePk.USR_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiUserProfilePk.PROFILE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String profile;

	@PuiGenerated
	public PuiUserProfilePk() {
	}

	@PuiGenerated
	public PuiUserProfilePk(String profile, String usr) {
		this.profile = profile;
		this.usr = usr;
	}

	@PuiGenerated
	@Override
	public String getUsr() {
		return usr;
	}

	@PuiGenerated
	@Override
	public void setUsr(String usr) {
		this.usr = usr;
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
	public PuiUserProfilePk createPk() {
		PuiUserProfilePk pk = new PuiUserProfilePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
