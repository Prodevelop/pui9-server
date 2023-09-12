<#compress>
package ${config.server.basePackage}.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface I${config.selectedView.javaName} extends IViewDto {

<#list config.selectedView.columns as column>
	@PuiGenerated
	String ${column.dbName?upper_case}_COLUMN = "${column.dbName?lower_case}";
	@PuiGenerated
	String ${column.dbName?upper_case}_FIELD = "${column.javaName}";
</#list>

<#list config.selectedView.columns as column>
	@PuiGenerated
	<#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> get${column.javaName?cap_first}();
	@PuiGenerated
	void set${column.javaName?cap_first}(<#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> ${column.javaName});
</#list>
}
</#compress>
