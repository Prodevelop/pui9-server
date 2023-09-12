package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiLanguage extends IViewDto {
	@PuiGenerated
	String ISOCODE_COLUMN = "isocode";
	@PuiGenerated
	String ISOCODE_FIELD = "isocode";
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
	String getIsocode();

	@PuiGenerated
	void setIsocode(String isocode);

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
