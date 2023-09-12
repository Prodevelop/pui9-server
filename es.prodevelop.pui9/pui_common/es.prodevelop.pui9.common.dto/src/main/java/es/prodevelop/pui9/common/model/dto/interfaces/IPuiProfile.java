package es.prodevelop.pui9.common.model.dto.interfaces;

import java.util.List;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiProfile extends IPuiProfilePk, IPuiProfileTra {
	String FUNCTIONALITIES_FIELD = "functionalities";

	List<IPuiFunctionality> getFunctionalities();

	void setFunctionalities(List<IPuiFunctionality> functionalities);
}
