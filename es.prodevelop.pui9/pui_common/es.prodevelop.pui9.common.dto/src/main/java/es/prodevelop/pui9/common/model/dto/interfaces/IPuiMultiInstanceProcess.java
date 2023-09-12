package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiMultiInstanceProcess extends IPuiMultiInstanceProcessPk {
	@PuiGenerated
	String PERIOD_COLUMN = "period";
	@PuiGenerated
	String PERIOD_FIELD = "period";
	@PuiGenerated
	String TIME_UNIT_COLUMN = "time_unit";
	@PuiGenerated
	String TIME_UNIT_FIELD = "timeunit";
	@PuiGenerated
	String INSTANCE_ASSIGNEE_UUID_COLUMN = "instance_assignee_uuid";
	@PuiGenerated
	String INSTANCE_ASSIGNEE_UUID_FIELD = "instanceassigneeuuid";
	@PuiGenerated
	String LATEST_EXECUTION_COLUMN = "latest_execution";
	@PuiGenerated
	String LATEST_EXECUTION_FIELD = "latestexecution";
	@PuiGenerated
	String LATEST_HEARTBEAT_COLUMN = "latest_heartbeat";
	@PuiGenerated
	String LATEST_HEARTBEAT_FIELD = "latestheartbeat";

	@PuiGenerated
	Integer getPeriod();

	@PuiGenerated
	void setPeriod(Integer period);

	@PuiGenerated
	String getTimeunit();

	@PuiGenerated
	void setTimeunit(String timeunit);

	@PuiGenerated
	String getInstanceassigneeuuid();

	@PuiGenerated
	void setInstanceassigneeuuid(String instanceassigneeuuid);

	@PuiGenerated
	java.time.Instant getLatestexecution();

	@PuiGenerated
	void setLatestexecution(java.time.Instant latestexecution);

	@PuiGenerated
	java.time.Instant getLatestheartbeat();

	@PuiGenerated
	void setLatestheartbeat(java.time.Instant latestheartbeat);
}