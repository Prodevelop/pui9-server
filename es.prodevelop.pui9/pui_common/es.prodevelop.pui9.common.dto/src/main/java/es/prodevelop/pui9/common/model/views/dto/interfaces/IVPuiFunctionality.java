package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiFunctionality extends IViewDto {
	@PuiGenerated
	String FUNCTIONALITY_COLUMN = "functionality";
	@PuiGenerated
	String FUNCTIONALITY_FIELD = "functionality";
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String SUBSYSTEM_COLUMN = "subsystem";
	@PuiGenerated
	String SUBSYSTEM_FIELD = "subsystem";
	@PuiGenerated
	String SUBSYSTEM_NAME_COLUMN = "subsystem_name";
	@PuiGenerated
	String SUBSYSTEM_NAME_FIELD = "subsystemname";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getFunctionality();

	@PuiGenerated
	void setFunctionality(String functionality);

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getSubsystem();

	@PuiGenerated
	void setSubsystem(String subsystem);

	@PuiGenerated
	String getSubsystemname();

	@PuiGenerated
	void setSubsystemname(String subsystemname);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
