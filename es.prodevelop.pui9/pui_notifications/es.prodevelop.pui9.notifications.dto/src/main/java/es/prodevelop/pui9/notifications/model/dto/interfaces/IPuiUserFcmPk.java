package es.prodevelop.pui9.notifications.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiUserFcmPk extends ITableDto {
	@PuiGenerated
	String TOKEN_COLUMN = "token";
	@PuiGenerated
	String TOKEN_FIELD = "token";

	@PuiGenerated
	String getToken();

	@PuiGenerated
	void setToken(String token);
}
