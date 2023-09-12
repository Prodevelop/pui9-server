package es.prodevelop.pui9.alerts.enums;

import org.apache.commons.lang3.ObjectUtils;

public enum AlertConfigTimeBeforeAfterEnum {

	BEFORE,

	AFTER;

	public static AlertConfigTimeBeforeAfterEnum getValue(String value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}

		for (AlertConfigTimeBeforeAfterEnum val : values()) {
			if (val.name().equals(value)) {
				return val;
			}
		}

		return null;
	}

}
