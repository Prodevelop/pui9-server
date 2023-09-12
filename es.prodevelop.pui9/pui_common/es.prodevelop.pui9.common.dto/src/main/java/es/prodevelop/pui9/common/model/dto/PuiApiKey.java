package es.prodevelop.pui9.common.model.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKey;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.GeometryType;

@PuiEntity(tablename = "pui_api_key")
@PuiGenerated
public class PuiApiKey extends PuiApiKeyPk implements IPuiApiKey {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiApiKey.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IPuiApiKey.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IPuiApiKey.PROFILE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String profile;

	@PuiGenerated
	@Override
	public String getName() {
		return name;
	}

	@PuiGenerated
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@PuiGenerated
	@Override
	public String getDescription() {
		return description;
	}

	@PuiGenerated
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@PuiGenerated
	@Override
	public String getProfile() {
		return profile;
	}

	@PuiGenerated
	@Override
	public void setProfile(String profile) {
		this.profile = profile;
	}

	private transient Set<String> functionalities;

	@Override
	public Set<String> getFunctionalities() {
		if (functionalities == null) {
			functionalities = new LinkedHashSet<>();
		}
		return functionalities;
	}

	@Override
	public void setFunctionalities(Set<String> functionalities) {
		this.functionalities = functionalities;
	}

}
