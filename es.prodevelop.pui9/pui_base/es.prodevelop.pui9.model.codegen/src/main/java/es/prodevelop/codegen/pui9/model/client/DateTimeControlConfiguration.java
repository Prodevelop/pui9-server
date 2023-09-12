package es.prodevelop.codegen.pui9.model.client;

public class DateTimeControlConfiguration extends AbstractExtendedControlConfiguration
		implements IReadOnlyControlConfiguration {

	private Boolean readOnly = false;
	private Boolean withTime = true;
	private Boolean withSeconds = false;
	private Boolean today = false;
	private String dateFormat = null;

	@Override
	public Boolean getReadOnly() {
		return readOnly;
	}

	@Override
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getWithTime() {
		return withTime;
	}

	public void setWithTime(Boolean withTime) {
		this.withTime = withTime;
	}

	public Boolean getWithSeconds() {
		return withSeconds;
	}

	public void setWithSeconds(Boolean withSeconds) {
		this.withSeconds = withSeconds;
	}

	public Boolean getToday() {
		return today;
	}

	public void setToday(Boolean today) {
		this.today = today;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}
