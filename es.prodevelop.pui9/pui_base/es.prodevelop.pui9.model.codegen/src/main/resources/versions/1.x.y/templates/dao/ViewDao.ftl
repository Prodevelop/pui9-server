<#compress>
package ${config.server.basePackage}.model.views.dao;

import es.prodevelop.pui9.annotations.PuiGenerated;
<#assign superclass = "">
<#if config.server.geoProject && config.selectedView.withGeometry>
import es.prodevelop.pui9.geo.dao.AbstractGeoViewDao;
	<#assign superclass = "AbstractGeoViewDao">
<#else>
import es.prodevelop.pui9.model.dao.AbstractViewDao;
	<#assign superclass = "AbstractViewDao">
</#if>

import ${config.server.basePackage}.model.views.dao.interfaces.I${config.selectedView.javaName}Dao;
import ${config.server.basePackage}.model.views.dto.interfaces.I${config.selectedView.javaName};

import es.prodevelop.pui9.exceptions.PuiDaoFindException;

import org.springframework.stereotype.Repository;

@PuiGenerated
@Repository
public class ${config.selectedView.javaName}Dao extends ${superclass}<I${config.selectedView.javaName}> implements I${config.selectedView.javaName}Dao {
<#list config.selectedView.columns as column>
	<#if !column.javaClassName?has_content>
	@PuiGenerated
	@Override
	public java.util.List<I${config.selectedView.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException {
		return super.findByColumn(I${config.selectedView.javaName}<#if column.pk>Pk</#if>.${column.dbName?upper_case}_FIELD, ${column.javaName});
	}
	</#if>
</#list>
}
</#compress>
