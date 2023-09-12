package es.prodevelop.pui9.common.model.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_profile", tabletranslationname = "pui_profile_tra")
public class PuiProfile extends PuiProfilePk implements IPuiProfile {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiProfile.LANG_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = true, isgeometry = false, issequence = false)
	private String lang;
	@PuiGenerated
	@PuiField(columnname = IPuiProfile.LANG_STATUS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = true, isgeometry = false, issequence = false)
	private Integer langstatus = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiProfile.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = true, isgeometry = false, issequence = false)
	private String name;

	@PuiGenerated
	@Override
	public String getLang() {
		return lang;
	}

	@PuiGenerated
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}

	@PuiGenerated
	@Override
	public Integer getLangstatus() {
		return langstatus;
	}

	@PuiGenerated
	@Override
	public void setLangstatus(Integer langstatus) {
		this.langstatus = langstatus;
	}

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

	private List<IPuiFunctionality> functionalities = new ArrayList<>();

	@Override
	public List<IPuiFunctionality> getFunctionalities() {
		return functionalities;
	}

	@Override
	public void setFunctionalities(List<IPuiFunctionality> functionalities) {
		this.functionalities = functionalities;
	}
}
