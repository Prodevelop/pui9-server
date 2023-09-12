package es.prodevelop.pui9.common.model.dto.interfaces;

import java.util.Set;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiApiKey extends IPuiApiKeyPk {
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";
	@PuiGenerated
	String PROFILE_COLUMN = "profile";
	@PuiGenerated
	String PROFILE_FIELD = "profile";

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);

	@PuiGenerated
	String getProfile();

	@PuiGenerated
	void setProfile(String profile);

	Set<String> getFunctionalities();

	void setFunctionalities(Set<String> functionalities);
}
