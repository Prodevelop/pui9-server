<#compress>
package ${config.server.basePackage}.model.dto;

<#assign dtoHasColumns = (config.selectedTable.columns?size > config.selectedTable.primaryKeys?size)>
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.utils.PuiObjectUtils;

import es.prodevelop.pui9.model.dto.AbstractTableDto;
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;

@PuiGenerated
public class ${config.selectedTable.javaName}Pk extends AbstractTableDto implements I${config.selectedTable.javaName}Pk {

	@PuiGenerated
	private static final long serialVersionUID = 1L;

<#list config.selectedTable.primaryKeys as pk>
	@PuiGenerated
	@PuiField(columnname = I${config.selectedTable.javaName}Pk.${pk.dbName?upper_case}_COLUMN, ispk = true, nullable = false, type = ColumnType.${pk.columnType}, autoincrementable = ${pk.autoincrementable?c}, maxlength = ${pk.javaSize?c}, islang = false, isgeometry = ${pk.geometry?c}, issequence = ${pk.sequence?c})
	private ${pk.javaTypeString} ${pk.javaName}<#if pk.javaDefaultValue?has_content> = ${pk.javaDefaultValue}</#if>;
</#list>

	@PuiGenerated
	public ${config.selectedTable.javaName}Pk() {
	}

<#assign params = "">
<#list config.selectedTable.primaryKeys as pk>
	<#assign params += pk.javaTypeString + " "+ pk.javaName>
	<#if pk?has_next>
		<#assign params += ", ">
	</#if>
</#list>

	@PuiGenerated
	public ${config.selectedTable.javaName}Pk(${params}) {
<#list config.selectedTable.primaryKeys as pk>
		this.${pk.javaName} = ${pk.javaName};
</#list>
	}

<#list config.selectedTable.primaryKeys as pk>
	@PuiGenerated
	@Override
	public ${pk.javaTypeString} get${pk.javaName?cap_first}() {
		return ${pk.javaName};
	}

	@PuiGenerated
	@Override
	public void set${pk.javaName?cap_first}(${pk.javaTypeString} ${pk.javaName}) {
		this.${pk.javaName} = ${pk.javaName};
	}
</#list>

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public ${config.selectedTable.javaName}Pk createPk() {
		${config.selectedTable.javaName}Pk pk = new ${config.selectedTable.javaName}Pk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
</#compress>
