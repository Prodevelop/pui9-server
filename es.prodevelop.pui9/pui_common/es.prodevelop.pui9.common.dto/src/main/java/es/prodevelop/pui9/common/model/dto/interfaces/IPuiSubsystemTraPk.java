package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiSubsystemTraPk extends ITableDto {
	@PuiGenerated
	String SUBSYSTEM_COLUMN = "subsystem";
	@PuiGenerated
	String SUBSYSTEM_FIELD = "subsystem";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getSubsystem();

	@PuiGenerated
	void setSubsystem(String subsystem);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
