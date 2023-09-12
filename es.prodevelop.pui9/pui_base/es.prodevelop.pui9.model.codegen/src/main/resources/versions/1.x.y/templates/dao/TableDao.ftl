<#compress>
package ${config.server.basePackage}.model.dao;

<#assign superclass = "">
<#if config.server.geoProject && config.selectedTable.withGeometry>
import es.prodevelop.pui9.geo.dao.AbstractGeoTableDao;
	<#assign superclass = "AbstractGeoTableDao">
<#else>
import es.prodevelop.pui9.model.dao.AbstractTableDao;
	<#assign superclass = "AbstractTableDao">
</#if>
<#if config.selectedTable.withAutowhere>
import es.prodevelop.pui9.filter.FilterBuilder;
</#if>

import ${config.server.basePackage}.model.dao.interfaces.I${config.selectedTable.javaName}Dao;
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName};
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;

import org.springframework.stereotype.Repository;

@PuiGenerated
@Repository
public class ${config.selectedTable.javaName}Dao extends ${superclass}<I${config.selectedTable.javaName}Pk, I${config.selectedTable.javaName}> implements I${config.selectedTable.javaName}Dao {
<#list config.selectedTable.columns as column>
	<#if !column.javaClassName?has_content>
	@PuiGenerated
	@Override
	public java.util.List<I${config.selectedTable.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException {
		return super.findByColumn(I${config.selectedTable.javaName}<#if column.pk>Pk</#if>.${column.dbName?upper_case}_FIELD, ${column.javaName});
	}
	</#if>
</#list>

<#if config.selectedTable.withAutowhere>
	/**
	 * Mark this method as @generated NOT if you modify it
	 * 
	 * @generated
	 */
	@Override
	protected FilterBuilder getAutoincrementableColumnFilter(I${config.selectedTable.javaName} dto, String columnName) {
		FilterBuilder filterBuilder = FilterBuilder.newAndFilter();
	<#list config.selectedTable.columns as column>
		<#if column.autowhere??>
		if (columnName.equals(I${config.selectedTable.javaName}.${column.dbName?upper_case}_COLUMN)) {
			<#list column.autowhere?split(",") as split>
			filterBuilder.addEquals(I${config.selectedTable.javaName}.${split?upper_case}_COLUMN, dto.get${split?cap_first}());
			</#list>
		}
		</#if>
	</#list>
		return filterBuilder;
	}
</#if>
}
</#compress>
