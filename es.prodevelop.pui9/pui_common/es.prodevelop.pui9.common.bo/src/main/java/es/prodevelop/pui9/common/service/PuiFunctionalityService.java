package es.prodevelop.pui9.common.service;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiFunctionalityDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiFunctionalityDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiFunctionality;
import es.prodevelop.pui9.common.service.interfaces.IPuiFunctionalityService;
import es.prodevelop.pui9.service.AbstractService;

@PuiGenerated
@Service
public class PuiFunctionalityService extends
		AbstractService<IPuiFunctionalityPk, IPuiFunctionality, IVPuiFunctionality, IPuiFunctionalityDao, IVPuiFunctionalityDao>
		implements IPuiFunctionalityService {
}