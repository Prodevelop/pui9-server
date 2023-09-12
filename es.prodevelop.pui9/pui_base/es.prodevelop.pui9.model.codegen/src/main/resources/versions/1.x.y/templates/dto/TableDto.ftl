<#compress>
package ${config.server.basePackage}.model.dto;

<#assign dtoHasColumns = (config.selectedTable.columns?size > config.selectedTable.primaryKeys?size)>
import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.GeometryType;
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName};
<#if config.selectedTable.translationTable??>
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.translationTable.javaName};
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.translationTable.javaName}Pk;
</#if>

@PuiGenerated
@PuiEntity(tablename = "${config.selectedTable.dbName?lower_case}"<#if (config.selectedTable.translationTable??)>, tabletranslationname = "${config.selectedTable.translationTable.dbName?lower_case}"</#if>)
public class ${config.selectedTable.javaName} extends ${config.selectedTable.javaName}Pk implements I${config.selectedTable.javaName} {

	@PuiGenerated
	private static final long serialVersionUID = 1L;

<#list config.selectedTable.columns as column>
	<#if column.pk>
		<#continue>
	</#if>
	@PuiGenerated
	@PuiField(columnname = I${config.selectedTable.javaName}.${column.dbName?upper_case}_COLUMN, ispk = false, nullable = ${column.nullable?c}, type = ColumnType.${column.columnType}, autoincrementable = ${column.autoincrementable?c}, maxlength = ${column.javaSize?c}, islang = false, isgeometry = ${column.geometry?c}, geometrytype = GeometryType.${column.geometryType}, issequence = ${column.sequence?c})
	private <#if column.javaClassName?has_content>${column.javaClassName}<#else>${column.javaTypeString}</#if> ${column.javaName}<#if !column.javaClassName?has_content && column.javaDefaultValue?has_content> = ${column.javaDefaultValue}</#if>;
</#list>

<#if config.selectedTable.translationTable??>
	<#list config.selectedTable.translationTable.columns as langColumn>
		<#if langColumn.pk && langColumn.dbName != "lang">
			<#continue>
		</#if>
	@PuiGenerated
	@PuiField(columnname = I${config.selectedTable.translationTable.javaName}<#if langColumn.dbName == "lang">Pk</#if>.${langColumn.dbName?upper_case}_COLUMN, ispk = false, nullable = ${langColumn.nullable?c}, type = ColumnType.${langColumn.columnType}, autoincrementable = ${langColumn.autoincrementable?c}, maxlength = ${langColumn.javaSize?c}, islang = true, isgeometry = ${langColumn.geometry?c}, issequence = ${langColumn.sequence?c})
	private <#if langColumn.javaClassName?has_content>${langColumn.javaClassName}<#else>${langColumn.javaTypeString}</#if> ${langColumn.javaName}<#if !langColumn.javaClassName?has_content && langColumn.javaDefaultValue?has_content> = ${langColumn.javaDefaultValue}</#if>;
	</#list>
</#if>

<#if dtoHasColumns>
	<#list config.selectedTable.columns as column>
		<#if column.pk>
			<#continue>
		</#if>
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
</#if>

<#if config.selectedTable.translationTable??>
	<#list config.selectedTable.translationTable.columns as langColumn>
		<#if langColumn.pk && langColumn.dbName != "lang">
			<#continue>
		</#if>
	@PuiGenerated
	@Override
	public <#if langColumn.javaClassName?has_content>${langColumn.javaClassName}<#else>${langColumn.javaTypeString}</#if> get${langColumn.javaName?cap_first}() {
		return ${langColumn.javaName};
	}

	@PuiGenerated
	@Override
	public void set${langColumn.javaName?cap_first}(<#if langColumn.javaClassName?has_content>${langColumn.javaClassName}<#else>${langColumn.javaTypeString}</#if> ${langColumn.javaName}) {
		this.${langColumn.javaName} = ${langColumn.javaName};
	}
	</#list>
</#if>

}
</#compress>
