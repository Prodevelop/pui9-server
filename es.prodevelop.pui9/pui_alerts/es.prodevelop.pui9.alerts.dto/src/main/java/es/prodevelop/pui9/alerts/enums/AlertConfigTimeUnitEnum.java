package es.prodevelop.pui9.alerts.enums;

import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.ObjectUtils;

public enum AlertConfigTimeUnitEnum {

	MINUTES,

	HOURS,

	DAYS;

	public static ChronoUnit toChronoUnit(String alertTimeUnit) {
		if (ObjectUtils.isEmpty(alertTimeUnit)) {
			return null;
		}

		return toChronoUnit(AlertConfigTimeUnitEnum.valueOf(alertTimeUnit));
	}

	public static ChronoUnit toChronoUnit(AlertConfigTimeUnitEnum alertTimeUnit) {
		if (alertTimeUnit == null) {
			return null;
		}

		return ChronoUnit.valueOf(alertTimeUnit.name());
	}

}
