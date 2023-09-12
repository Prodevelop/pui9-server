package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTraPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiProfileTraPk extends AbstractTableDto implements IPuiProfileTraPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiProfileTraPk.PROFILE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String profile;
	@PuiGenerated
	@PuiField(columnname = IPuiProfileTraPk.LANG_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String lang;

	@PuiGenerated
	public PuiProfileTraPk() {
	}

	@PuiGenerated
	public PuiProfileTraPk(String lang, String profile) {
		this.lang = lang;
		this.profile = profile;
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
	@SuppressWarnings("unchecked")
	public PuiProfileTraPk createPk() {
		PuiProfileTraPk pk = new PuiProfileTraPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
