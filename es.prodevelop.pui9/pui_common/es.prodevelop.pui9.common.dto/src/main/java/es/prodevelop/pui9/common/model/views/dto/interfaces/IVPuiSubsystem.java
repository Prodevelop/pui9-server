package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiSubsystem extends IViewDto {
	@PuiGenerated
	String SUBSYSTEM_COLUMN = "subsystem";
	@PuiGenerated
	String SUBSYSTEM_FIELD = "subsystem";
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getSubsystem();

	@PuiGenerated
	void setSubsystem(String subsystem);

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
