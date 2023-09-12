package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMultiInstanceProcess;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_multi_instance_process")
public class PuiMultiInstanceProcess extends PuiMultiInstanceProcessPk implements IPuiMultiInstanceProcess {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiMultiInstanceProcess.PERIOD_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer period;
	@PuiGenerated
	@PuiField(columnname = IPuiMultiInstanceProcess.TIME_UNIT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 15, islang = false, isgeometry = false, issequence = false)
	private String timeunit;
	@PuiGenerated
	@PuiField(columnname = IPuiMultiInstanceProcess.INSTANCE_ASSIGNEE_UUID_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String instanceassigneeuuid;
	@PuiGenerated
	@PuiField(columnname = IPuiMultiInstanceProcess.LATEST_EXECUTION_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant latestexecution;
	@PuiGenerated
	@PuiField(columnname = IPuiMultiInstanceProcess.LATEST_HEARTBEAT_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant latestheartbeat;

	@PuiGenerated
	@Override
	public Integer getPeriod() {
		return period;
	}

	@PuiGenerated
	@Override
	public void setPeriod(Integer period) {
		this.period = period;
	}

	@PuiGenerated
	@Override
	public String getTimeunit() {
		return timeunit;
	}

	@PuiGenerated
	@Override
	public void setTimeunit(String timeunit) {
		this.timeunit = timeunit;
	}

	@PuiGenerated
	@Override
	public String getInstanceassigneeuuid() {
		return instanceassigneeuuid;
	}

	@PuiGenerated
	@Override
	public void setInstanceassigneeuuid(String instanceassigneeuuid) {
		this.instanceassigneeuuid = instanceassigneeuuid;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getLatestexecution() {
		return latestexecution;
	}

	@PuiGenerated
	@Override
	public void setLatestexecution(java.time.Instant latestexecution) {
		this.latestexecution = latestexecution;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getLatestheartbeat() {
		return latestheartbeat;
	}

	@PuiGenerated
	@Override
	public void setLatestheartbeat(java.time.Instant latestheartbeat) {
		this.latestheartbeat = latestheartbeat;
	}
}