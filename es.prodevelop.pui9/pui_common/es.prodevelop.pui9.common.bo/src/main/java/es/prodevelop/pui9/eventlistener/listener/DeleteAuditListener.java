package es.prodevelop.pui9.eventlistener.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.audit.dto.AuditOneRegistry;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.DeleteEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Listener for Delete an element
 */
@Component
public class DeleteAuditListener extends AbstractAuditListener<DeleteEvent> {

	@Override
	protected String getType() {
		return "delete";
	}

	@Override
	protected boolean fillAudit(DeleteEvent event, IPuiAudit puiAudit) {
		ITableDto dto = event.getSource();
		puiAudit.setPk(getDtoPKAsString(dto));

		List<AuditOneRegistry> list = AuditOneRegistry.processOneRegistry(dto);
		String json = GsonSingleton.getSingleton().getGson().toJson(list);
		puiAudit.setContent(json);
		return !list.isEmpty();
	}

}
