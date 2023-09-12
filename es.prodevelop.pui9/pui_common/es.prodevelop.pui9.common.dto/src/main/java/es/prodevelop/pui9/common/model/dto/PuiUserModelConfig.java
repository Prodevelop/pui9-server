package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_user_model_config")
public class PuiUserModelConfig extends PuiUserModelConfigPk implements IPuiUserModelConfig {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelConfig.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelConfig.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelConfig.CONFIGURATION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String configuration;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelConfig.TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String type;

	@PuiGenerated
	@Override
	public String getUsr() {
		return usr;
	}

	@PuiGenerated
	@Override
	public void setUsr(String usr) {
		this.usr = usr;
	}

	@PuiGenerated
	@Override
	public String getModel() {
		return model;
	}

	@PuiGenerated
	@Override
	public void setModel(String model) {
		this.model = model;
	}

	@PuiGenerated
	@Override
	public String getConfiguration() {
		return configuration;
	}

	@PuiGenerated
	@Override
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	@PuiGenerated
	@Override
	public String getType() {
		return type;
	}

	@PuiGenerated
	@Override
	public void setType(String type) {
		this.type = type;
	}
}
