package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiApiKeyPk extends ITableDto {
	@PuiGenerated
	String API_KEY_COLUMN = "api_key";
	@PuiGenerated
	String API_KEY_FIELD = "apikey";

	@PuiGenerated
	String getApikey();

	@PuiGenerated
	void setApikey(String apikey);
}
