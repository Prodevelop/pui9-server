package es.prodevelop.pui9.alerts.enums;

import org.apache.commons.lang3.ObjectUtils;

public enum AlertConfigTypeEnum {

	GENERIC,

	MODEL;

	public static AlertConfigTypeEnum getValue(String value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}

		for (AlertConfigTypeEnum val : values()) {
			if (val.name().equals(value)) {
				return val;
			}
		}

		return null;
	}

}
