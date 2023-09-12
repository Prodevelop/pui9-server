package es.prodevelop.pui9.eventlistener.listener;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;

import es.prodevelop.pui9.common.model.dto.PuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.service.interfaces.IPuiAuditService;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Abstract implementation of Audit Activity Listener. Registers into PuiAudit
 * table all the actions that affects to the activated DAOs
 */
public abstract class AbstractAuditListener<E extends PuiEvent<?>> extends PuiListener<E> {

	private static final String PK_SEPARATOR = "#";

	@Autowired
	protected IPuiAuditService auditService;

	@Override
	protected boolean passFilter(E eventObject) {
		return eventObject.getSource() != null;
	}

	@Override
	protected void process(E event) throws PuiException {
		PuiUserSession userSession = PuiUserSession.getCurrentSession();

		IPuiAudit puiAudit = new PuiAudit();
		puiAudit.setUsr(userSession != null ? userSession.getUsr() : "NO_USER");
		puiAudit.setDatetime(Instant.now());
		puiAudit.setIp(userSession != null ? userSession.getIp() : "NO_IP");
		puiAudit.setType(getType());
		puiAudit.setClient(userSession != null ? userSession.getClient() : "NO_CLIENT");
		if (event.getSource() instanceof ITableDto) {
			puiAudit.setModel(DtoRegistry.getEntityFromDto(((ITableDto) event.getSource()).getClass()));
		}

		boolean shouldAudit = fillAudit(event, puiAudit);
		if (!shouldAudit) {
			return;
		}

		try {
			auditService.insert(puiAudit);
		} catch (Exception e) {
			// some error when inserting the audit activity
		}
	}

	/**
	 * Generate a PuiAudit object to insert it into the PUI Audit table in the
	 * database. By default usr, datetime and type values was filled previously.
	 */
	protected abstract boolean fillAudit(E event, IPuiAudit puiAudit);

	/**
	 * Get the type of the operation
	 * 
	 * @return The operation type
	 */
	protected abstract String getType();

	/**
	 * Get the PK of the given DTO in an specific format: pk1#pk2#...#pkN
	 */
	protected String getDtoPKAsString(IDto dto) {
		if (dto == null) {
			return "";
		}

		List<String> fieldNames = DtoRegistry.getPkFields(dto.getClass());
		StringBuilder pkValue = new StringBuilder();
		for (Iterator<String> it = fieldNames.iterator(); it.hasNext();) {
			Field field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), it.next());
			try {
				Object value = FieldUtils.readField(field, dto, true);
				String strValue = "";
				if (value != null) {
					strValue = value.toString();
				} else {
					strValue = "null";
				}
				pkValue.append(strValue);
				if (it.hasNext()) {
					pkValue.append(PK_SEPARATOR);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		}

		return pkValue.toString();
	}

}
