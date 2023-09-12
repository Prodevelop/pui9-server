package es.prodevelop.pui9.dashboards.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.dashboards.dto.EChartsLineBarOptions;
import es.prodevelop.pui9.dashboards.dto.EChartsPieOptions;
import es.prodevelop.pui9.dashboards.dto.EChartsTypes;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidget;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetPk;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiWidget;
import es.prodevelop.pui9.dashboards.service.interfaces.IPuiWidgetService;
import es.prodevelop.pui9.filter.FilterGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI Widgets")
@RequestMapping("/puiwidget")
public class PuiWidgetController extends
		AbstractCommonController<IPuiWidgetPk, IPuiWidget, IVPuiWidget, IPuiWidgetDao, IVPuiWidgetDao, IPuiWidgetService> {
	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_WIDGET";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_WIDGET";
	}

	@Operation(summary = "Get ECharts Pie options", description = "Get ECharts Pie options")
	@PostMapping(value = "/getEChartsPieOptions", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public EChartsPieOptions getEChartsPieOptions(@RequestParam String entity, @RequestParam String name,
			@RequestParam String value, @RequestParam(required = false) String tooltip,
			@RequestBody(required = false) FilterGroup filter) {
		return getService().getEChartsPieOptions(entity, name, value, tooltip, filter);
	}

	@Operation(summary = "Get ECharts Line/Bar options", description = "Get ECharts Line/Bar options")
	@PostMapping(value = "/getEChartsLineBarOptions", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public EChartsLineBarOptions getEChartsLineBarOptions(@RequestParam String entity, @RequestParam String name,
			@RequestParam String value, @RequestParam(required = false) String tooltip, @RequestParam EChartsTypes type,
			@RequestBody(required = false) FilterGroup filter) {
		return getService().getEChartsLineBarOptions(entity, name, value, tooltip, type, filter);
	}

	@Operation(summary = "Get Vuetify Datatable values", description = "Get Vuetify Datatable values")
	@PostMapping(value = "/getVuetifyDatatableValues", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<Object> getVuetifyDatatableValues(@RequestParam String entity,
			@RequestBody(required = false) FilterGroup filter) {
		return getService().getVuetifyDatatableValues(entity, filter);
	}

	@Operation(summary = "Get Vuetify Datatable count", description = "Get Vuetify Datatable count")
	@PostMapping(value = "/getVuetifyDatatableCount", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Long getVuetifyDatatableCount(@RequestParam String entity,
			@RequestBody(required = false) FilterGroup filter) {
		return getService().getVuetifyDatatableCount(entity, filter);
	}
}