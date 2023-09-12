<#compress>
package ${config.server.basePackage}.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
<#assign hasTranslation = config.selectedTable.translationTable??>
public interface I${config.selectedTable.javaName} extends I${config.selectedTable.javaName}Pk <#if hasTranslation>, I${config.selectedTable.translationTable.javaName}</#if> {

<#list config.selectedTable.columns as column>
	<#if column.pk>
		<#continue>
	</#if>
	@PuiGenerated
	String ${column.dbName?upper_case}_COLUMN = "${column.dbName?lower_case}";
	@PuiGenerated
	String ${column.dbName?upper_case}_FIELD = "${column.javaName}";
</#list>

<#list config.selectedTable.columns as column>
	<#if column.pk>
		<#continue>
	</#if>
	@PuiGenerated
	<#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> get${column.javaName?cap_first}();
	@PuiGenerated
	void set${column.javaName?cap_first}(<#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> ${column.javaName});
</#list>
}
</#compress>
