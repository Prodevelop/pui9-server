package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiUserModelConfig extends IPuiUserModelConfigPk {
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String CONFIGURATION_COLUMN = "configuration";
	@PuiGenerated
	String CONFIGURATION_FIELD = "configuration";
	@PuiGenerated
	String TYPE_COLUMN = "type";
	@PuiGenerated
	String TYPE_FIELD = "type";

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getConfiguration();

	@PuiGenerated
	void setConfiguration(String configuration);

	@PuiGenerated
	String getType();

	@PuiGenerated
	void setType(String type);
}
