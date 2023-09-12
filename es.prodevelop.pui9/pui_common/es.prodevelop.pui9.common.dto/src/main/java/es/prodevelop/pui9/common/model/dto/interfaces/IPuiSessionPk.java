package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiSessionPk extends ITableDto {
	@PuiGenerated
	String UUID_COLUMN = "uuid";
	@PuiGenerated
	String UUID_FIELD = "uuid";

	@PuiGenerated
	String getUuid();

	@PuiGenerated
	void setUuid(String uuid);
}
