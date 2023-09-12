package es.prodevelop.pui9.utils;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to deal with time datatype. By default, server side will always
 * work with Instant UTC TimeZone. Here you can convert Dates into Instants and
 * vice versa, and parsing or formatting values
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDateUtil {

	public static final ZoneId utcZone = ZoneId.of("UTC");
	public static final ZoneId europeMadridZone = ZoneId.of("Europe/Madrid");
	public static final DateTimeFormatter isoInstantMilliseconds = new DateTimeFormatterBuilder().appendInstant(3)
			.toFormatter();
	public static final DateTimeFormatter isoInstantNoMilliseconds = DateTimeFormatter.ISO_INSTANT
			.withZone(ZoneId.systemDefault());
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String DEFAULT_FORMAT_WITH_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static final String SEP = "{sep}";

	/**
	 * Convert an String into a Instant in UTC zone. Multiple formats are accepted,
	 * see {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value The datetime in string format
	 * @return The parsed Instant
	 */
	public static Instant stringToInstant(String value) {
		return stringToInstant(value, null);
	}

	/**
	 * Convert an String into a LocalDate. Multiple formats are accepted, see
	 * {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value  The datetime in string format
	 * @param zoneId The zoneId to be used. If null, ZoneId.systemDefault() will be
	 *               used
	 * @return The parsed LocalDate
	 */
	public static LocalDate stringToLocalDate(String value, ZoneId zoneId) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		String dateFormat = getDateFormat(value);
		if (ObjectUtils.isEmpty(dateFormat)) {
			return null;
		}

		if (zoneId == null) {
			zoneId = ZoneId.systemDefault();
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(zoneId);
		try {
			return LocalDate.parse(value, formatter);
		} catch (DateTimeException e) {
			return null;
		}
	}

	/**
	 * Convert an String into LocalDateTime. Multiple formats are accepted, see
	 * {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value  The datetime in string format
	 * @param zoneId The zoneId to convert value to that zone
	 * @return The parsed LocalDateTime
	 */
	public static LocalDateTime stringToLocalDateTime(String value, ZoneId zoneId) {
		if (value.contains("T") && value.contains("Z")) {
			Instant instant = Instant.parse(value);
			return LocalDateTime.ofInstant(instant, zoneId);
		} else {
			String dateFormat = getDateFormat(value);
			if (ObjectUtils.isEmpty(dateFormat)) {
				return null;
			}

			String timeFormat = getTimeFormat(value, dateFormat);
			if (ObjectUtils.isEmpty(timeFormat)) {
				LocalDate ld = stringToLocalDate(value, zoneId);
				return ld != null ? ld.atStartOfDay() : null;
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat + timeFormat).withZone(zoneId);
				try {
					return LocalDateTime.parse(value, formatter);
				} catch (DateTimeException e) {
					formatter = DateTimeFormatter.ofPattern(dateFormat + " " + timeFormat).withZone(zoneId);
					try {
						return LocalDateTime.parse(value, formatter);
					} catch (DateTimeException e2) {
						return null;
					}
				}
			}
		}
	}

	/**
	 * Convert an String into ZonedDateTime. Multiple formats are accepted, see
	 * {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value  The datetime in string format
	 * @param zoneId The zoneId to convert value to that zone
	 * @return The parsed ZonedDateTime
	 */
	public static ZonedDateTime stringToZonedDateTime(String value, ZoneId zoneId) {
		if (value.contains("T") && value.contains("Z")) {
			Instant instant = Instant.parse(value);
			return instant.atZone(zoneId);
		} else {
			String dateFormat = getDateFormat(value);
			if (ObjectUtils.isEmpty(dateFormat)) {
				return null;
			}

			String timeFormat = getTimeFormat(value, dateFormat);
			if (ObjectUtils.isEmpty(timeFormat)) {
				LocalDate ld = stringToLocalDate(value, zoneId);
				return ld != null ? ld.atStartOfDay(zoneId) : null;
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat + timeFormat).withZone(zoneId);
				try {
					return ZonedDateTime.parse(value, formatter);
				} catch (DateTimeException e1) {
					formatter = DateTimeFormatter.ofPattern(dateFormat + " " + timeFormat).withZone(zoneId);
					try {
						return ZonedDateTime.parse(value, formatter);
					} catch (DateTimeException e2) {
						return null;
					}
				}
			}
		}
	}

	/**
	 * Convert an String into a Instant in UTC zone. Multiple formats are accepted,
	 * see {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value     The datetime in string format
	 * @param formatter The formatter to be used
	 * @return The parsed Instant
	 */
	public static Instant stringToInstant(String value, DateTimeFormatter formatter) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		if (formatter == null) {
			// first try to parse using utc default formatter
			try {
				return Instant.from(isoInstantMilliseconds.parse(value));
			} catch (DateTimeException e) {
				try {
					return Instant.from(isoInstantNoMilliseconds.parse(value));
				} catch (DateTimeException e2) {
					// do nothing
				}
			}

			String dateFormat = getDateFormat(value);
			if (ObjectUtils.isEmpty(dateFormat)) {
				return null;
			}

			String timeFormat = getTimeFormat(value, dateFormat);
			if (ObjectUtils.isEmpty(timeFormat)) {
				formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.systemDefault());
				try {
					return LocalDate.parse(value, formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
				} catch (DateTimeException e) {
					return null;
				}
			} else {
				formatter = DateTimeFormatter.ofPattern(dateFormat + timeFormat).withZone(ZoneId.systemDefault());
				try {
					return LocalDateTime.parse(value, formatter).atZone(ZoneId.systemDefault()).toInstant();
				} catch (DateTimeException e1) {
					formatter = DateTimeFormatter.ofPattern(dateFormat + " " + timeFormat)
							.withZone(ZoneId.systemDefault());
					try {
						return LocalDateTime.parse(value, formatter).atZone(ZoneId.systemDefault()).toInstant();
					} catch (DateTimeException e2) {
						return null;
					}
				}
			}
		} else {
			try {
				return Instant.from(formatter.parse(value));
			} catch (DateTimeException e) {
				return null;
			}
		}
	}

	/**
	 * Check if the given date value has specified the hours
	 * 
	 * @param value The date value
	 * @return True if hours are specified; false if not
	 */
	public static boolean stringHasHours(String value) {
		String dateFormat = getDateFormat(value);
		if (ObjectUtils.isEmpty(dateFormat)) {
			return false;
		}

		String timeFormat = getTimeFormat(value, dateFormat);
		if (ObjectUtils.isEmpty(timeFormat)) {
			return false;
		}

		return TimeFormats.HOUR2.matchFormat(timeFormat) || TimeFormats.HOUR1.matchFormat(timeFormat)
				|| TimeFormats.HOUR2_MINUTE.matchFormat(timeFormat) || TimeFormats.HOUR1_MINUTE.matchFormat(timeFormat)
				|| TimeFormats.HOUR2_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR1_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_TZ.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_MILLISECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_MILLISECOND_TZ.matchFormat(timeFormat);
	}

	/**
	 * Check if the given date value has specified the minutes
	 * 
	 * @param value The date value
	 * @return True if minutes are specified; false if not
	 */
	public static boolean stringHasMinutes(String value) {
		String dateFormat = getDateFormat(value);
		if (ObjectUtils.isEmpty(dateFormat)) {
			return false;
		}

		String timeFormat = getTimeFormat(value, dateFormat);
		if (ObjectUtils.isEmpty(timeFormat)) {
			return false;
		}

		return TimeFormats.HOUR2_MINUTE.matchFormat(timeFormat) || TimeFormats.HOUR1_MINUTE.matchFormat(timeFormat)
				|| TimeFormats.HOUR2_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR1_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_TZ.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_MILLISECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_MILLISECOND_TZ.matchFormat(timeFormat);
	}

	/**
	 * Check if the given date value has specified the seconds
	 * 
	 * @param value The date value
	 * @return True if seconds are specified; false if not
	 */
	public static boolean stringHasSeconds(String value) {
		String dateFormat = getDateFormat(value);
		if (ObjectUtils.isEmpty(dateFormat)) {
			return false;
		}

		String timeFormat = getTimeFormat(value, dateFormat);
		if (ObjectUtils.isEmpty(timeFormat)) {
			return false;
		}

		return TimeFormats.HOUR2_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR1_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_TZ.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_MILLISECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_MILLISECOND_TZ.matchFormat(timeFormat);
	}

	/**
	 * Convert a Temporal Accessor into a String using a default formatter
	 *
	 * @param temporalAccessor The temporal accessor to be converted
	 * @return The formatted String
	 */
	public static String temporalAccessorToString(TemporalAccessor temporalAccessor) {
		return temporalAccessorToString(temporalAccessor, null);
	}

	/**
	 * Convert a Instant into a String with the given formatter. If no formatter was
	 * specified, a default one is used depending on the type of the temporal
	 * accessor
	 *
	 * @param temporalAccessor The temporal accessor to be converted
	 * @param formatter        The formatter to be used. Can be null
	 * @return The formatted String
	 */
	public static String temporalAccessorToString(TemporalAccessor temporalAccessor, DateTimeFormatter formatter) {
		if (temporalAccessor == null) {
			return null;
		}

		if (formatter == null) {
			if (temporalAccessor instanceof Instant) {
				formatter = isoInstantMilliseconds;
			} else if (temporalAccessor instanceof ZonedDateTime) {
				formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
			} else if (temporalAccessor instanceof LocalDate) {
				formatter = DateTimeFormatter.ISO_LOCAL_DATE;
			} else if (temporalAccessor instanceof LocalDateTime) {
				formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			} else if (temporalAccessor instanceof OffsetDateTime) {
				formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
			}
		} else {
			if (!(temporalAccessor instanceof ZonedDateTime) && formatter.getZone() == null) {
				formatter = formatter.withZone(ZoneId.systemDefault());
			}
		}

		return formatter != null ? formatter.format(temporalAccessor) : null;
	}

	/**
	 * Get the given instant at the given zone id
	 * 
	 * @param instant The instant
	 * @param zoneId  The zone Id
	 * @return The instant at the zone id
	 */
	public static ZonedDateTime getInstantAtZoneId(Instant instant, ZoneId zoneId) {
		if (instant == null) {
			return null;
		}
		return instant.atZone(zoneId);
	}

	/**
	 * Converts a falsy Instant to a real Instant, based on the given zoneId. It
	 * means that the falsy Instant is really the LocalDateTime of the given zoneId,
	 * so you want to convert this to the real Instant at UTC
	 * 
	 * @param falsyInstant The date time of the given ZoneID
	 * @param zoneId       The ZoneID
	 * @return The real instant in UTC
	 */
	public static Instant getRealInstantFromFalsyInstant(Instant falsyInstant, ZoneId zoneId) {
		return LocalDateTime.ofInstant(falsyInstant, ZoneId.systemDefault()).atZone(zoneId).toInstant();
	}

	private static String getDateFormat(String value) {
		for (DateSeparators sep : DateSeparators.values()) {
			for (DateFormats df : DateFormats.values()) {
				String template = df.template.replace(SEP, sep.sep);
				if (value.matches(template)) {
					return df.format.replace(SEP, sep.sep);
				}
			}
		}

		return null;
	}

	private static String getTimeFormat(String value, String dateFormat) {
		if (dateFormat == null) {
			return null;
		}

		DateFormats df = DateFormats.getByFormat(dateFormat);
		if (df == null) {
			return null;
		}

		DateSeparators ds = DateSeparators.guessSeparator(dateFormat);
		if (ds == null) {
			return null;
		}

		String time = value.replaceAll(df.template.replace(".*", "").replace(SEP, ds.sep), "").trim();
		for (TimeFormats tf : TimeFormats.values()) {
			if (time.matches(tf.template)) {
				return tf.format;
			}
		}

		return null;
	}

	private enum DateSeparators {
		SLASH("/"),

		DASH("-"),

		SPACE(" ");

		private final String sep;

		private DateSeparators(String sep) {
			this.sep = sep;
		}

		public static DateSeparators guessSeparator(String dateFormat) {
			for (DateSeparators ds : values()) {
				if (dateFormat.contains(ds.sep)) {
					return ds;
				}
			}
			return null;
		}
	}

	private enum DateFormats {
		DAY_MONTH_YEAR("\\d{1,2}" + SEP + "\\d{1,2}" + SEP + "\\d{4}.*", "dd" + SEP + "MM" + SEP + "yyyy"),

		YEAR_MONTH("\\d{4}" + SEP + "\\d{1,2}" + SEP + "\\d{1,2}.*", "yyyy" + SEP + "MM" + SEP + "dd");

		private final String template;
		private final String format;

		private DateFormats(String template, String format) {
			this.template = template;
			this.format = format;
		}

		public static DateFormats getByFormat(String format) {
			for (DateSeparators sep : DateSeparators.values()) {
				if (format.contains(sep.sep)) {
					format = format.replace(sep.sep, SEP);
					break;
				}
			}
			for (DateFormats df : values()) {
				if (df.format.equals(format)) {
					return df;
				}
			}

			return null;
		}
	}

	private enum TimeFormats {
		HOUR_MINUTE_SECOND_MILLISECOND(".*\\d{2}:\\d{1,2}:\\d{1,2}.\\d{1,3}", "HH:mm:ss.SSS"),

		HOUR_MINUTE_SECOND_MILLISECOND_TZ("[T]\\d{2}:\\d{1,2}:\\d{1,2}.\\d{1,3}[Z]", "'T'HH:mm:ss.SSS'Z'"),

		HOUR_MINUTE_SECOND_TZ("[T]\\d{2}:\\d{1,2}:\\d{1,2}[Z]", "'T'HH:mm:ss'Z'"),

		HOUR2_MINUTE_SECOND(".*\\d{2}:\\d{1,2}:\\d{1,2}", "HH:mm:ss"),

		HOUR1_MINUTE_SECOND(".*\\d{1}:\\d{1,2}:\\d{1,2}", "H:mm:ss"),

		HOUR2_MINUTE(".*\\d{2}:\\d{1,2}", "HH:mm"),

		HOUR1_MINUTE(".*\\d{1}:\\d{1,2}", "H:mm"),

		HOUR2(".*\\d{2}", "HH"),

		HOUR1(".*\\d{1}", "H"),

		NO_TIME("", "");

		private final String template;
		private final String format;

		private TimeFormats(String template, String format) {
			this.template = template;
			this.format = format;
		}

		public boolean matchFormat(String format) {
			return this.format.equals(format);
		}
	}

}