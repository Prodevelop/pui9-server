<#compress>
package ${config.server.basePackage}.model.views.dto;

import ${config.server.basePackage}.model.views.dto.interfaces.I${config.selectedView.javaName};

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.enums.GeometryType;

import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "${config.selectedView.dbName?lower_case}")
public class ${config.selectedView.javaName} extends AbstractViewDto implements I${config.selectedView.javaName} {

	@PuiGenerated
	private static final long serialVersionUID = 1L;

<#list config.selectedView.columns as column>
	@PuiGenerated
	@PuiField(columnname = I${config.selectedView.javaName}.${column.dbName?upper_case}_COLUMN, ispk = false, nullable = ${column.nullable?c}, type = ColumnType.${column.columnType}, autoincrementable = ${column.autoincrementable?c}, maxlength = ${column.javaSize?c}, islang = false, isgeometry = ${column.geometry?c}, geometrytype = GeometryType.${column.geometryType}, issequence = ${column.sequence?c})
	@PuiViewColumn(order = ${column?index + 1}, visibility = ColumnVisibility.${column.columnVisibility})
	private <#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> ${column.javaName};
</#list>

<#list config.selectedView.columns as column>
	@PuiGenerated
	@Override
	public <#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> get${column.javaName?cap_first}() {
		return ${column.javaName};
	}

	@PuiGenerated
	@Override
	public void set${column.javaName?cap_first}(<#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> ${column.javaName}) {
		this.${column.javaName} = ${column.javaName};
	}
</#list>

}
</#compress>
