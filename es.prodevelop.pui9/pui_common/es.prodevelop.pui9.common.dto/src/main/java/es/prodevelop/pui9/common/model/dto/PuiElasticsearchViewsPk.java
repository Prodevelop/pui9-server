package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViewsPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiElasticsearchViewsPk extends AbstractTableDto implements IPuiElasticsearchViewsPk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiElasticsearchViewsPk.APPNAME_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String appname = "DEFAULT";
	@PuiGenerated
	@PuiField(columnname = IPuiElasticsearchViewsPk.VIEWNAME_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String viewname;

	@PuiGenerated
	public PuiElasticsearchViewsPk(String appname, String viewname) {
		this.appname = appname;
		this.viewname = viewname;
	}

	@PuiGenerated
	public PuiElasticsearchViewsPk() {
	}

	@PuiGenerated
	@Override
	public String getAppname() {
		return appname;
	}

	@PuiGenerated
	@Override
	public void setAppname(String appname) {
		this.appname = appname;
	}

	@PuiGenerated
	@Override
	public String getViewname() {
		return viewname;
	}

	@PuiGenerated
	@Override
	public void setViewname(String viewname) {
		this.viewname = viewname;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiElasticsearchViewsPk createPk() {
		PuiElasticsearchViewsPk pk = new PuiElasticsearchViewsPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
