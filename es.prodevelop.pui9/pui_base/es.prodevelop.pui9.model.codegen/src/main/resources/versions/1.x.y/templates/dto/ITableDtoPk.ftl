<#compress>
package ${config.server.basePackage}.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface I${config.selectedTable.javaName}Pk extends ITableDto {

<#list config.selectedTable.primaryKeys as pk>
	@PuiGenerated
	String ${pk.dbName?upper_case}_COLUMN = "${pk.dbName?lower_case}";
	@PuiGenerated
	String ${pk.dbName?upper_case}_FIELD = "${pk.javaName}";

</#list>

<#list config.selectedTable.primaryKeys as pk>
	@PuiGenerated
	${pk.javaTypeString} get${pk.javaName?cap_first}();
	@PuiGenerated
	void set${pk.javaName?cap_first}(${pk.javaTypeString} ${pk.javaName});

</#list>
}
</#compress>
