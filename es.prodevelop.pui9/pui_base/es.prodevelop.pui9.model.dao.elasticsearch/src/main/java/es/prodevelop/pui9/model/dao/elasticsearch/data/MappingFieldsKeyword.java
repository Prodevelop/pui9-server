package es.prodevelop.pui9.model.dao.elasticsearch.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MappingFieldsKeyword {

	private MappingTypesEnum type = MappingTypesEnum._keyword;

	public MappingTypesEnum getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return this.hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcBuilder = new HashCodeBuilder();
		hcBuilder.append(type);

		return hcBuilder.hashCode();
	}

	@Override
	public String toString() {
		return type.toString();
	}

}