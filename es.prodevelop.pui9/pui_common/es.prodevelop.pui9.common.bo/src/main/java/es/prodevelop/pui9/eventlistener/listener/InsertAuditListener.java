package es.prodevelop.pui9.eventlistener.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.audit.dto.AuditOneRegistry;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.InsertEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Listener for Insert an element
 */
@Component
public class InsertAuditListener extends AbstractAuditListener<InsertEvent> {

	@Override
	protected String getType() {
		return "insert";
	}

	@Override
	protected boolean fillAudit(InsertEvent event, IPuiAudit puiAudit) {
		ITableDto dto = event.getSource();
		puiAudit.setPk(getDtoPKAsString(dto));

		List<AuditOneRegistry> list = AuditOneRegistry.processOneRegistry(dto);
		String json = GsonSingleton.getSingleton().getGson().toJson(list);
		puiAudit.setContent(json);
		return !list.isEmpty();
	}

}
