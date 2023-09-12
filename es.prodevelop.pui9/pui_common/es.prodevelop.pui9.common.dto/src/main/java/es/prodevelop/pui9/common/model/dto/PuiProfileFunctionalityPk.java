package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionalityPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiProfileFunctionalityPk extends AbstractTableDto implements IPuiProfileFunctionalityPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiProfileFunctionalityPk.PROFILE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String profile;
	@PuiGenerated
	@PuiField(columnname = IPuiProfileFunctionalityPk.FUNCTIONALITY_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String functionality;

	@PuiGenerated
	public PuiProfileFunctionalityPk() {
	}

	@PuiGenerated
	public PuiProfileFunctionalityPk(String functionality, String profile) {
		this.functionality = functionality;
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
	public String getFunctionality() {
		return functionality;
	}

	@PuiGenerated
	@Override
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiProfileFunctionalityPk createPk() {
		PuiProfileFunctionalityPk pk = new PuiProfileFunctionalityPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
