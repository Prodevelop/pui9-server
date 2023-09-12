<#compress>
package ${config.server.basePackage}.controller;

<#assign className = "">
<#assign tableDaoClass = "">
<#assign tableDtoClass = "">
<#assign tableDtoPkClass = "">
<#assign viewDaoClass = "">
<#assign viewDtoClass = "">
<#assign serviceClass = "">
<#if config.selectedTable??>
	<#assign className = config.selectedTable.javaName>
	<#assign tableDaoClass = "I" + config.selectedTable.javaName + "Dao">
import ${config.server.basePackage}.model.dao.interfaces.I${config.selectedTable.javaName}Dao;
	<#assign tableDtoClass = "I" + config.selectedTable.javaName>
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName};
	<#assign tableDtoPkClass = "I" + config.selectedTable.javaName + "Pk">
import ${config.server.basePackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;
<#else>
	<#assign tableDaoClass = "INullTableDao">
import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
	<#assign tableDtoClass = "INullTable">
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
	<#assign tableDtoPkClass = "INullTablePk">
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
</#if>

<#if config.selectedView??>
	<#if !className?has_content>
		<#assign className = config.selectedView.javaName>
	</#if>
	<#assign viewDaoClass = "I" + config.selectedView.javaName + "Dao">
import ${config.server.basePackage}.model.views.dao.interfaces.I${config.selectedView.javaName}Dao;
	<#assign viewDtoClass = "I" + config.selectedView.javaName>
import ${config.server.basePackage}.model.views.dto.interfaces.I${config.selectedView.javaName};
<#else>
	<#assign viewDaoClass = "INullViewDao">
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
	<#assign viewDtoClass = "INullView">
import es.prodevelop.pui9.model.dto.interfaces.INullView;
</#if>

<#assign serviceClass = "I" + className + "Service">
import es.prodevelop.pui9.annotations.PuiGenerated;
import ${config.server.basePackage}.service.interfaces.${serviceClass};
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@PuiGenerated
@Controller
@RequestMapping("/${config.modelName}")
public class ${className}Controller extends ${config.server.superController}<${tableDtoPkClass}, ${tableDtoClass}, ${viewDtoClass}, ${tableDaoClass}, ${viewDaoClass}, ${serviceClass}> {

<#if !config.server.useAdvancedFunctionalities>
	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "${config.server.readFunctionality}";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "${config.server.writeFunctionality}";
	}
<#else>
	@PuiGenerated
	@Override
	protected String getInsertFunctionality() {
		return "${config.server.insertFunctionality}";
	}

	@PuiGenerated
	@Override
	protected String getUpdateFunctionality() {
		return "${config.server.updateFunctionality}";
	}
	@PuiGenerated
	@Override
	protected String getDeleteFunctionality() {
		return "${config.server.deleteFunctionality}";
	}

	@PuiGenerated
	@Override
	protected String getGetFunctionality() {
		return "${config.server.getFunctionality}";
	}

	@PuiGenerated
	@Override
	protected String getListFunctionality() {
		return "${config.server.listFunctionality}";
	}
</#if>

}
</#compress>
