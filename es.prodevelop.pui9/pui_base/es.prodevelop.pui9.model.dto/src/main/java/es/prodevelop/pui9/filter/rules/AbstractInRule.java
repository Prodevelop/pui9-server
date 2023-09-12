package es.prodevelop.pui9.filter.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public abstract class AbstractInRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	protected AbstractInRule(String field, FilterRuleOperation op) {
		super(field, op);
	}

	@Override
	@SuppressWarnings("unchecked")
	public AbstractInRule withData(Object data) {
		Collection<?> collection = new ArrayList<>();
		if (data instanceof String) {
			collection = Arrays.asList(StringUtils.split((String) data, ","));
		} else if (data instanceof Collection) {
			collection = (Collection<?>) data;
		}

		if (ObjectUtils.isEmpty(collection)) {
			return null;
		}

		Optional<?> first = collection.stream().findFirst();
		if (!first.isPresent()) {
			return null;
		}

		if (first.get() instanceof Number || first.get() instanceof String) {
			super.withData(collection);
		} else {
			collection = collection.stream().map(v -> v.toString()).collect(Collectors.toList());
			super.withData(collection);
		}
		return this;
	}

	public <T> Collection<T> getCollection() {
		return getData();
	}

}
