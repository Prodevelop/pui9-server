<#compress>
package ${config.server.basePackage}.model.dao.interfaces;

import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName};
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;

@PuiGenerated
public interface I${config.selectedTable.javaName}Dao extends ITableDao<I${config.selectedTable.javaName}Pk, I${config.selectedTable.javaName}> {
<#list config.selectedTable.columns as column>
	<#if !column.javaClassName?has_content>
	@PuiGenerated
	java.util.List<I${config.selectedTable.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException;
	</#if>
</#list>
}
</#compress>
