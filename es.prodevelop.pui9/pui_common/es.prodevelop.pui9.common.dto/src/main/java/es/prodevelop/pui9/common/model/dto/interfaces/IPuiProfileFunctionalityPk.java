package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiProfileFunctionalityPk extends ITableDto {
	@PuiGenerated
	String PROFILE_COLUMN = "profile";
	@PuiGenerated
	String PROFILE_FIELD = "profile";
	@PuiGenerated
	String FUNCTIONALITY_COLUMN = "functionality";
	@PuiGenerated
	String FUNCTIONALITY_FIELD = "functionality";

	@PuiGenerated
	String getProfile();

	@PuiGenerated
	void setProfile(String profile);

	@PuiGenerated
	String getFunctionality();

	@PuiGenerated
	void setFunctionality(String functionality);
}
