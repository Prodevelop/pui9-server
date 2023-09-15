package es.prodevelop.pui9.filter;

import java.time.temporal.ChronoUnit;

public class TodayRuleData {

	private TodayRuleTypeEnum type;
	private String sign;
	private String unitValue;
	private TodayRuleUnitTypeEnum unitType;

	public TodayRuleTypeEnum getType() {
		return type;
	}

	public String getSign() {
		return sign;
	}

	public String getUnitValue() {
		return unitValue;
	}

	public TodayRuleUnitTypeEnum getUnitType() {
		return unitType;
	}

	public enum TodayRuleTypeEnum {
		TODAY,

		NOW;
	}

	public enum TodayRuleUnitTypeEnum {
		HOURS(ChronoUnit.HOURS),

		DAYS(ChronoUnit.DAYS),

		WEEKS(ChronoUnit.WEEKS),

		MONTHS(ChronoUnit.MONTHS),

		YEARS(ChronoUnit.YEARS);

		public final ChronoUnit unit;

		private TodayRuleUnitTypeEnum(ChronoUnit unit) {
			this.unit = unit;
		}

	}

}
