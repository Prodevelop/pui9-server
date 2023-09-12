package es.prodevelop.pui9.model.dao.elasticsearch.data;

import com.google.gson.annotations.SerializedName;

public enum MappingTypesEnum {
	@SerializedName("text")
	_text,

	@SerializedName("long")
	_long,

	@SerializedName("double")
	_double,

	@SerializedName("date")
	_date,

	@SerializedName("boolean")
	_boolean,

	@SerializedName("keyword")
	_keyword,

	@SerializedName("geo_point")
	_geo_point,

	@SerializedName("geo_shape")
	_geo_shape;

	@Override
	public String toString() {
		return name();
	}

}