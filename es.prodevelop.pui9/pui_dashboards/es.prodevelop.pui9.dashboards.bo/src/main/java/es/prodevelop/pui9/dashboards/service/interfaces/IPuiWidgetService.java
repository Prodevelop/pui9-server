package es.prodevelop.pui9.dashboards.service.interfaces;

import java.util.List;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.dto.EChartsLineBarOptions;
import es.prodevelop.pui9.dashboards.dto.EChartsPieOptions;
import es.prodevelop.pui9.dashboards.dto.EChartsTypes;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidget;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetPk;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiWidget;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.service.interfaces.IService;

@PuiGenerated
public interface IPuiWidgetService
		extends IService<IPuiWidgetPk, IPuiWidget, IVPuiWidget, IPuiWidgetDao, IVPuiWidgetDao> {

	EChartsPieOptions getEChartsPieOptions(String entity, String columnName, String columnValue, String tooltip,
			FilterGroup filter);

	EChartsLineBarOptions getEChartsLineBarOptions(String entity, String columnName, String columnValue, String tooltip,
			EChartsTypes type, FilterGroup filter);

	List<Object> getVuetifyDatatableValues(String entity, FilterGroup filter);

	Long getVuetifyDatatableCount(String entity, FilterGroup filter);
}