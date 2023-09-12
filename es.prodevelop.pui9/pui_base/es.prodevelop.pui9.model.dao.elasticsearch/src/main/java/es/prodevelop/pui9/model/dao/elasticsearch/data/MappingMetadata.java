package es.prodevelop.pui9.model.dao.elasticsearch.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MappingMetadata {

	private Map<String, MappingField> properties;

	public void addMappingField(String field, MappingField definition) {
		if (properties == null) {
			properties = new LinkedHashMap<>();
		}
		properties.put(field, definition);
	}

	public Map<String, MappingField> getProperties() {
		return properties;
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
		List<String> list = new ArrayList<>(properties.keySet());
		Collections.sort(list);

		for (String key : list) {
			hcBuilder.append(key);
			hcBuilder.append(":");
			hcBuilder.append(properties.get(key));
			hcBuilder.append("; ");
		}

		return hcBuilder.hashCode();
	}

	@Override
	public String toString() {
		return properties.keySet().toString();
	}

}