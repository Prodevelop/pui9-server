package es.prodevelop.pui9.spring.listeners;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenModelDao;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;

@Component
public class DocgenOnApplicationRefreshListener {

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiModelService modelService;

	@Autowired
	private IPuiDocgenModelDao docgenModelDao;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!Objects.equals(event.getApplicationContext(), PuiApplicationContext.getInstance().getAppContext())) {
			return;
		}

		generateCodeOfModels();
	}

	private void generateCodeOfModels() {
		new Thread(() -> {
			List<String> models;
			try {
				models = docgenModelDao.findAll().stream().map(dm -> dm.getModel()).collect(Collectors.toList());
			} catch (PuiDaoFindException e) {
				models = Collections.emptyList();
			}

			if (ObjectUtils.isEmpty(models)) {
				return;
			}

			List<String> entities;
			try {
				entities = modelService.getTableDao()
						.findWhere(FilterBuilder.newAndFilter().addIn(IPuiModelPk.MODEL_COLUMN, models)).stream()
						.map(m -> m.getEntity()).collect(Collectors.toList());
			} catch (PuiDaoFindException e) {
				entities = Collections.emptyList();
			}

			entities.forEach(e -> daoRegistry.getDtoFromEntityName(e, false, true));
		}, "GENERATE_CODE_DOCGEN_MODELS").start();
	}

}