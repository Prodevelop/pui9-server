package es.prodevelop.pui9.audit.dto;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;

import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.IPuiObject;

public class AuditTwoRegistries extends AuditOneRegistry {

	private static final long serialVersionUID = 1L;

	public static List<AuditTwoRegistries> processTwoRegistries(IPuiObject oldObject, IPuiObject newObject) {
		BeanMap map = new BeanMap(oldObject);
		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		List<AuditTwoRegistries> list = new ArrayList<>();

		for (Object propNameObject : map.keySet()) {
			try {
				String propertyName = (String) propNameObject;
				if (propertyName.equalsIgnoreCase("class")) {
					continue;
				}

				Object oldValue = propUtils.getProperty(oldObject, propertyName);
				Object newValue = propUtils.getProperty(newObject, propertyName);

				if (oldValue instanceof BigDecimal && newValue instanceof BigDecimal) {
					oldValue = ((BigDecimal) oldValue).stripTrailingZeros();
					newValue = ((BigDecimal) newValue).stripTrailingZeros();
				}

				if (!Objects.equals(oldValue, newValue)) {
					list.add(new AuditTwoRegistries(propertyName, oldValue, newValue));
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// do nothing
			}
		}

		Collections.sort(list);

		return list;
	}

	public static List<AuditTwoRegistries> processTwoRegistries(IPuiObject oldObject,
			Map<String, Object> fieldValuesMap) {
		BeanMap map = new BeanMap(oldObject);
		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		List<AuditTwoRegistries> list = new ArrayList<>();

		for (Object propNameObject : map.keySet()) {
			String propertyName = (String) propNameObject;
			String columnName = oldObject instanceof IDto
					? DtoRegistry.getColumnNameFromFieldName(((IDto) oldObject).getClass(), propertyName)
					: "";
			if (propertyName.equalsIgnoreCase("class")) {
				continue;
			}
			if (!fieldValuesMap.containsKey(propertyName) && !fieldValuesMap.containsKey(columnName)) {
				continue;
			}

			try {
				Object oldValue = propUtils.getProperty(oldObject, propertyName);
				Object newValue = null;

				if (fieldValuesMap.containsKey(propertyName)) {
					newValue = fieldValuesMap.get(propertyName);
				} else if (fieldValuesMap.containsKey(columnName)) {
					newValue = fieldValuesMap.get(columnName);
				}

				if (oldValue instanceof BigDecimal && newValue instanceof BigDecimal) {
					oldValue = ((BigDecimal) oldValue).stripTrailingZeros();
					newValue = ((BigDecimal) newValue).stripTrailingZeros();
				}

				if (!Objects.equals(oldValue, newValue)) {
					list.add(new AuditTwoRegistries(propertyName, oldValue, newValue));
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// do nothing
			}
		}

		Collections.sort(list);

		return list;
	}

	private Object oldValue;

	protected AuditTwoRegistries(String attribute, Object oldValue, Object newValue) {
		super(attribute, newValue);
		this.oldValue = processValue(oldValue);
	}

	public Object getOldValue() {
		return oldValue;
	}

	@Override
	public String toString() {
		return getAttribute() + "::" + (oldValue != null ? oldValue.toString() : "null") + " -> "
				+ (getValue() != null ? getValue().toString() : "null");
	}

}