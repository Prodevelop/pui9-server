package es.prodevelop.pui9.model.dao.elasticsearch.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MappingField {

	private MappingTypesEnum type;
	private MappingFieldFields fields;
	private ESLanguagesEnum analyzer;
	private String format;

	public MappingField(MappingTypesEnum type, boolean withKeyword, ESLanguagesEnum analyzer) {
		this.type = type;

		if (type.equals(MappingTypesEnum._text) && withKeyword) {
			this.fields = new MappingFieldFields();
			this.analyzer = analyzer;
		} else if (type.equals(MappingTypesEnum._date)) {
			this.format = "strict_date_time";
		}
	}

	public MappingTypesEnum getType() {
		return type;
	}

	public MappingFieldFields getFields() {
		return fields;
	}

	public ESLanguagesEnum getAnalyzer() {
		return analyzer;
	}

	public String getFormat() {
		return format;
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
		if (fields != null) {
			hcBuilder.append(fields);
		}
		if (analyzer != null) {
			hcBuilder.append(analyzer);
		}
		if (format != null) {
			hcBuilder.append(format);
		}

		return hcBuilder.hashCode();
	}

	@Override
	public String toString() {
		return type.toString() + (fields != null ? ", " + fields.toString() : "")
				+ (analyzer != null ? ", " + analyzer.toString() : "") + (format != null ? ", " + format : "");
	}

}