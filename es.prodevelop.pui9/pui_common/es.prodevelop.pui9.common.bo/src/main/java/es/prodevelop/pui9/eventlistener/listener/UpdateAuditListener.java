package es.prodevelop.pui9.eventlistener.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.audit.dto.AuditTwoRegistries;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.UpdateEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@Component
public class UpdateAuditListener extends AbstractAuditListener<UpdateEvent> {

	@Override
	protected String getType() {
		return "update";
	}

	@Override
	protected boolean fillAudit(UpdateEvent event, IPuiAudit puiAudit) {
		ITableDto dto = event.getSource();
		puiAudit.setPk(getDtoPKAsString(dto));

		IDto oldDto = event.getOldDto();

		List<AuditTwoRegistries> list = AuditTwoRegistries.processTwoRegistries(oldDto, dto);

		String json = GsonSingleton.getSingleton().getGson().toJson(list);
		puiAudit.setContent(json);
		return !list.isEmpty();
	}

}
