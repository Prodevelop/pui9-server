package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiMenuPk extends ITableDto {
	@PuiGenerated
	String NODE_COLUMN = "node";
	@PuiGenerated
	String NODE_FIELD = "node";

	@PuiGenerated
	Integer getNode();

	@PuiGenerated
	void setNode(Integer node);
}
