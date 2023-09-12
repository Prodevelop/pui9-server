package es.prodevelop.pui9.eventlistener.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.audit.dto.AuditTwoRegistries;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.PatchEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

@Component
public class PatchAuditListener extends AbstractAuditListener<PatchEvent> {

	@Override
	protected String getType() {
		return "patch";
	}

	@Override
	protected boolean fillAudit(PatchEvent event, IPuiAudit puiAudit) {
		IDto dtoPk = event.getSource();
		puiAudit.setPk(getDtoPKAsString(dtoPk));

		List<AuditTwoRegistries> list = AuditTwoRegistries.processTwoRegistries(event.getOldDto(),
				event.getFieldValuesMap());

		String json = GsonSingleton.getSingleton().getGson().toJson(list);
		puiAudit.setContent(json);
		return !list.isEmpty();
	}

}
