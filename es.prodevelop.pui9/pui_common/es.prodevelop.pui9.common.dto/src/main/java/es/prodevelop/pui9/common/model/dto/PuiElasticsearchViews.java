package es.prodevelop.pui9.common.model.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_elasticsearch_views")
public class PuiElasticsearchViews extends PuiElasticsearchViewsPk implements IPuiElasticsearchViews {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiElasticsearchViews.IDENTITY_FIELDS_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String identityfields = "";

	@PuiGenerated
	@Override
	public String getIdentityfields() {
		return identityfields;
	}

	@PuiGenerated
	@Override
	public void setIdentityfields(String identityfields) {
		this.identityfields = identityfields;
	}

	@Override
	public String getParsedViewName() {
		return getViewname().toLowerCase();
	}

	@Override
	public List<String> getParsedIdFields() {
		return Stream.of(getIdentityfields().split(",", -1)).map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
	}

}
