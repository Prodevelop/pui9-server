package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUserFunctionality;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_user_functionality")
public class VPuiUserFunctionality extends AbstractViewDto implements IVPuiUserFunctionality {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiUserFunctionality.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IVPuiUserFunctionality.FUNCTIONALITY_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String functionality;
	@PuiGenerated
	@PuiField(columnname = IVPuiUserFunctionality.FUNCTIONALITY_NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String functionalityname;
	@PuiGenerated
	@PuiField(columnname = IVPuiUserFunctionality.PROFILE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String profile;
	@PuiGenerated
	@PuiField(columnname = IVPuiUserFunctionality.PROFILE_NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String profilename;

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
	public String getFunctionality() {
		return functionality;
	}

	@PuiGenerated
	@Override
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
	}

	@PuiGenerated
	@Override
	public String getFunctionalityname() {
		return functionalityname;
	}

	@PuiGenerated
	@Override
	public void setFunctionalityname(String functionalityname) {
		this.functionalityname = functionalityname;
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
	public String getProfilename() {
		return profilename;
	}

	@PuiGenerated
	@Override
	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}
}
