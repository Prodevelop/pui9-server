<#compress>
package ${config.server.basePackage}.model.views.dao.interfaces;

import ${config.server.basePackage}.model.views.dto.interfaces.I${config.selectedView.javaName};

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;

@PuiGenerated
public interface I${config.selectedView.javaName}Dao extends IViewDao<I${config.selectedView.javaName}> {
<#list config.selectedView.columns as column>
	<#if !column.javaClassName?has_content>
	@PuiGenerated
	java.util.List<I${config.selectedView.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException;
	</#if>
</#list>
}
</#compress>
