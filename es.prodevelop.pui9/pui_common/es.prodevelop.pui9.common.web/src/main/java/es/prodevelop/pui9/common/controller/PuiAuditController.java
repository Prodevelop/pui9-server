package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiAuditDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiAuditDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.common.service.interfaces.IPuiAuditService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.eventlistener.listener.AbstractAuditListener;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller adds support for checking the audit of the entities. Each
 * action of type insert/update/delete of any registry is stored in the
 * PUI_AUDIT table, via the {@link AbstractAuditListener} listeners of PUI
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI Audit")
@RequestMapping("/puiaudit")
public class PuiAuditController extends
		AbstractCommonController<IPuiAuditPk, IPuiAudit, IVPuiAudit, IPuiAuditDao, IVPuiAuditDao, IPuiAuditService> {
	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_AUDIT";
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@Override
	public boolean allowGet() {
		return false;
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowUpdate() {
		return false;
	}

	@Override
	public boolean allowExport() {
		return false;
	}

}