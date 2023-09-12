package es.prodevelop.pui9.model.dao.elasticsearch.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MappingFieldFields {

	private MappingFieldsKeyword keyword = new MappingFieldsKeyword();

	public MappingFieldsKeyword getKeyword() {
		return keyword;
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
		hcBuilder.append(keyword);

		return hcBuilder.hashCode();
	}

	@Override
	public String toString() {
		return keyword.toString();
	}

}