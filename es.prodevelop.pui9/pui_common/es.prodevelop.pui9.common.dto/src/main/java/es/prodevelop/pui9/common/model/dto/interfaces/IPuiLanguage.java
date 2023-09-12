package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiLanguage extends IPuiLanguagePk {
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String ISDEFAULT_COLUMN = "isdefault";
	@PuiGenerated
	String ISDEFAULT_FIELD = "isdefault";
	@PuiGenerated
	String ENABLED_COLUMN = "enabled";
	@PuiGenerated
	String ENABLED_FIELD = "enabled";

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	Integer getIsdefault();

	@PuiGenerated
	void setIsdefault(Integer isdefault);

	@PuiGenerated
	Integer getEnabled();

	@PuiGenerated
	void setEnabled(Integer enabled);
}
