package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

public interface IPuiElasticsearchViewsPk extends ITableDto {

	@PuiGenerated
	String APPNAME_COLUMN = "appname";

	@PuiGenerated
	String APPNAME_FIELD = "appname";

	@PuiGenerated
	String VIEWNAME_COLUMN = "viewname";

	@PuiGenerated
	String VIEWNAME_FIELD = "viewname";

	@PuiGenerated
	String getAppname();

	@PuiGenerated
	void setAppname(String appname);

	@PuiGenerated
	String getViewname();

	@PuiGenerated
	void setViewname(String viewname);
}
