package es.prodevelop.pui9.audit.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.IPuiObject;

public class AuditOneRegistry implements Serializable, Comparable<AuditOneRegistry> {

	private static final long serialVersionUID = 1L;

	public static List<AuditOneRegistry> processOneRegistry(IPuiObject puiObject) {
		BeanMap map = new BeanMap(puiObject);
		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		List<AuditOneRegistry> list = new ArrayList<>();

		for (Object propNameObject : map.keySet()) {
			String propertyName = (String) propNameObject;
			if (propertyName.equalsIgnoreCase("class")) {
				continue;
			}

			try {
				Object property = propUtils.getProperty(puiObject, propertyName);
				list.add(new AuditOneRegistry(propertyName, property));
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// do nothing
			}
		}

		Collections.sort(list);

		return list;
	}

	private String attribute;
	private Object value;

	protected AuditOneRegistry(String attribute, Object value) {
		this.attribute = attribute;
		this.value = processValue(value);
	}

	public String getAttribute() {
		return attribute;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return attribute + "::" + (value != null ? value.toString() : "null");
	}

	/**
	 * If the value is a list, if the value is a tableDto, convert it into its pk
	 */
	protected Object processValue(Object val) {
		Object processed;

		if (val instanceof List<?>) {
			List<Object> list = new ArrayList<>();
			for (Object obj : (List<?>) val) {
				if (obj instanceof ITableDto) {
					ITableDto pk = ((ITableDto) obj).createPk();
					List<AuditOneRegistry> sublist = processOneRegistry(pk);
					list.add(sublist);
				} else {
					list.add(obj);
				}
			}
			processed = list;
		} else {
			processed = val;
		}

		return processed;
	}

	@Override
	public int compareTo(AuditOneRegistry o) {
		return this.attribute.compareTo(o.attribute);
	}

}