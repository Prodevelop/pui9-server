package es.prodevelop.pui9.notifications.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.notifications.eventlistener.event.FcmTokenRegisteredEvent;
import es.prodevelop.pui9.notifications.eventlistener.event.FcmTokenUnregisteredEvent;
import es.prodevelop.pui9.notifications.model.dao.interfaces.IPuiUserFcmDao;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;
import es.prodevelop.pui9.notifications.service.interfaces.IPuiUserFcmService;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;

@PuiGenerated
@Service
public class PuiUserFcmService
		extends AbstractService<IPuiUserFcmPk, IPuiUserFcm, INullView, IPuiUserFcmDao, INullViewDao>
		implements IPuiUserFcmService {

	private static final Integer MAX_DAYS_WITHOUT_USE = 30;

	@Autowired
	private PuiFcmClient fcmClient;

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	/**
	 * Initializes a background task that is executed every night
	 */
	@PostConstruct
	private void postConstructUserFcmService() {
		Long initDelay = PuiMultiInstanceProcessBackgroundExecutors.getNextExecutionDelayAsMinutes(0, 0);

		multiInstanceProcessBackExec.registerNewExecutor("FcmPurgueTokens", initDelay, TimeUnit.DAYS.toMinutes(1),
				TimeUnit.MINUTES, this::purgueTokens);
	}

	@Override
	protected void beforeInsert(IPuiUserFcm dto) throws PuiServiceException {
		dto.setLastuse(Instant.now());
	}

	@Override
	protected void afterInsert(IPuiUserFcm dto) throws PuiServiceException {
		getEventLauncher().fireAsync(new FcmTokenRegisteredEvent(dto));
	}

	@Override
	protected void beforeUpdate(IPuiUserFcm oldDto, IPuiUserFcm dto) throws PuiServiceException {
		dto.setLastuse(Instant.now());
	}

	@Override
	protected void afterUpdate(IPuiUserFcm oldDto, IPuiUserFcm dto) throws PuiServiceException {
		if (!oldDto.getUsr().equals(dto.getUsr())) {
			getEventLauncher().fireAsync(new FcmTokenUnregisteredEvent(oldDto));
			getEventLauncher().fireAsync(new FcmTokenRegisteredEvent(dto));
		}
	}

	@Override
	protected void afterDelete(IPuiUserFcm dto) throws PuiServiceException {
		getEventLauncher().fireAsync(new FcmTokenUnregisteredEvent(dto));
	}

	@Override
	public void registerUserFcmToken(IPuiUserFcm userFcm) throws PuiServiceException {
		if (!exists(userFcm.createPk())) {
			insert(userFcm);
		} else {
			update(userFcm);
		}
	}

	@Override
	public void unregisterUserFcmToken(IPuiUserFcmPk userFcmPk) throws PuiServiceException {
		delete(userFcmPk);
	}

	/**
	 * Check all the FCM Tokens, and remove from the database those that are not
	 * valid
	 */
	private void purgueTokens() {
		Instant maxLastUse = Instant.now().minusSeconds(TimeUnit.MINUTES.toSeconds(MAX_DAYS_WITHOUT_USE));

		SearchRequest req = new SearchRequest();
		req.setRows(1000);
		req.setOrder(Collections.singletonList(Order.newOrderAsc(IPuiUserFcmPk.TOKEN_COLUMN)));
		req.setFilter(FilterBuilder.newAndFilter().addLowerEqualsThan(IPuiUserFcm.LAST_USE_COLUMN, maxLastUse)
				.asFilterGroup());

		getTableDao().executePaginagedOperation(req, null, list -> {
			List<String> tokens = list.stream().map(IPuiUserFcm::getToken).collect(Collectors.toList());
			List<String> toDelete = fcmClient.validateTokens(tokens);
			list.removeIf(t -> !toDelete.contains(t.getToken()));

			for (IPuiUserFcm uf : list) {
				try {
					delete(uf);
				} catch (PuiServiceException e) {
					// do nothing
				}
			}
		});
	}

}