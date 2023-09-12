package es.prodevelop.pui9.threads;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiMultiInstanceProcessDao;
import es.prodevelop.pui9.common.model.dto.PuiMultiInstanceProcess;
import es.prodevelop.pui9.common.model.dto.PuiMultiInstanceProcessPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMultiInstanceProcess;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;

/**
 * This class allows to create background processes that will be executed only
 * in one instance of the application. So it is targeted for those applications
 * that will be executed on an elastic architecture. If it's not your case, you
 * can basically use {@link PuiBackgroundExecutors} class instead.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiMultiInstanceProcessBackgroundExecutors {

	private static final Long HEARTBEAT_DELAY = 15L;

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private IPuiMultiInstanceProcessDao multiInstanceProcessDao;

	/**
	 * Unique UUID for the application instance, distinct on each execution
	 */
	private String uuid;

	/**
	 * Contains all the processes that belongs to this application. Useful if
	 * multiple applications uses the same database. Used to controll which
	 * processes can be assigned to this instance
	 */
	private Set<String> applicationProcessIdSet;

	@PostConstruct
	private void postConstruct() {
		uuid = UUID.randomUUID().toString();
		applicationProcessIdSet = new LinkedHashSet<>();
		logger.info("***** UUID for this instance: " + uuid + " *****");

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("MultiInstance_Process_Heartbeat", false,
				HEARTBEAT_DELAY, HEARTBEAT_DELAY, TimeUnit.SECONDS, this::updateHeartBeat);
	}

	/**
	 * Get the UUID for this instance
	 * 
	 * @return
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Get all the processes id that belongs to this application and that are
	 * susceptible to change to another instance of the same application
	 * 
	 * @return
	 */
	public Set<String> getAllProcessId() {
		return applicationProcessIdSet;
	}

	/**
	 * Registers a new executor that will be executed at the defined fixed delay
	 * (what means that subsequent executions will take part in the 'delay' timeUnit
	 * after the completion of the previous execution)
	 * 
	 * @param name         The name of the Executor that will be used for the
	 *                     created Thread
	 * @param initialDelay The initial execution delay before the first execution
	 * @param delay        The delay between subsequent executions
	 * @param unit         The unit of the delays
	 * @param runnable     The runnable to be executed
	 */
	public void registerNewExecutor(String name, long initialDelay, long delay, TimeUnit unit, Runnable runnable) {
		registerExecutor(name, delay, unit);

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("MultiInstance_" + name, false, initialDelay, delay,
				unit, () -> executeProcess(name, runnable));
	}

	/**
	 * Registers a new executor that will be executed at the defined fixed rate
	 * (what means that subsequent executions will take part in the fixed 'delay'
	 * timeUnit after the start of the previous execution)
	 * 
	 * @param name         The name of the Executor that will be used for the
	 *                     created Thread
	 * @param initialDelay The initial execution delay before the first execution
	 * @param delay        The delay between subsequent executions
	 * @param unit         The unit of the delays
	 * @param runnable     The runnable to be executed
	 */
	public void registerNewExecutorAtFixedRate(String name, long initialDelay, long delay, TimeUnit unit,
			Runnable runnable) {
		registerExecutor(name, delay, unit);

		PuiBackgroundExecutors.getSingleton().registerNewExecutorAtFixedRate("MultiInstance_" + name, false,
				initialDelay, delay, unit, () -> executeProcess(name, runnable));
	}

	private void registerExecutor(String name, long delay, TimeUnit unit) {
		applicationProcessIdSet.add(name);
		insertOrUpdateInTable(name, delay, unit);

		new Thread(() -> {
			try {
				int initDelay = SecureRandom.getInstanceStrong().nextInt(1000);
				Thread.sleep(initDelay);
			} catch (NoSuchAlgorithmException e) {
				// do nothing
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}

	private void updateHeartBeat() {
		List<IPuiMultiInstanceProcess> list;
		try {
			list = multiInstanceProcessDao.findAll();
		} catch (PuiDaoFindException e) {
			list = Collections.emptyList();
		}

		Instant now = Instant.now();
		list.forEach(pmip -> {
			if (pmip.getInstanceassigneeuuid().equals(uuid)) {
				try {
					pmip.setLatestheartbeat(now);

					multiInstanceProcessDao.patch(pmip.createPk(), Collections
							.singletonMap(IPuiMultiInstanceProcess.LATEST_HEARTBEAT_COLUMN, pmip.getLatestheartbeat()));
				} catch (PuiDaoSaveException e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				checkAsigneeAndHeartbeat(pmip, now);
			}
		});
	}

	private void insertOrUpdateInTable(String name, long delay, TimeUnit timeUnit) {
		IPuiMultiInstanceProcess pmip;

		try {
			pmip = multiInstanceProcessDao.findOne(new PuiMultiInstanceProcessPk(name));
		} catch (PuiDaoFindException e) {
			pmip = null;
			logger.error(e.getMessage(), e);
		}

		Instant now = Instant.now();
		if (pmip == null) {
			pmip = new PuiMultiInstanceProcess();
			pmip.setId(name);
			pmip.setPeriod((int) delay);
			pmip.setTimeunit(timeUnit.name());
			pmip.setInstanceassigneeuuid(uuid);
			pmip.setLatestheartbeat(now);

			try {
				multiInstanceProcessDao.insert(pmip);
			} catch (PuiDaoSaveException e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			pmip.setPeriod((int) delay);
			pmip.setTimeunit(timeUnit.name());

			Map<String, Object> map = new LinkedHashMap<>();
			map.put(IPuiMultiInstanceProcess.PERIOD_COLUMN, pmip.getPeriod());
			map.put(IPuiMultiInstanceProcess.TIME_UNIT_COLUMN, pmip.getTimeunit());

			try {
				multiInstanceProcessDao.patch(pmip.createPk(), map);
			} catch (PuiDaoSaveException e) {
				logger.error(e.getMessage(), e);
			}

			checkAsigneeAndHeartbeat(pmip, now);
		}
	}

	private void checkAsigneeAndHeartbeat(IPuiMultiInstanceProcess pmip, Instant now) {
		if (!applicationProcessIdSet.contains(pmip.getId())) {
			return;
		}

		if (Duration.between(pmip.getLatestheartbeat(), now).abs().getSeconds() > (HEARTBEAT_DELAY + 5)) {
			pmip.setInstanceassigneeuuid(uuid);
			pmip.setLatestheartbeat(now);

			Map<String, Object> map = new LinkedHashMap<>();
			map.put(IPuiMultiInstanceProcess.INSTANCE_ASSIGNEE_UUID_COLUMN, pmip.getInstanceassigneeuuid());
			map.put(IPuiMultiInstanceProcess.LATEST_HEARTBEAT_COLUMN, pmip.getLatestheartbeat());

			try {
				multiInstanceProcessDao.patch(pmip.createPk(), map);
				logger.info("Changing background process '" + pmip.getId() + "' to instance " + uuid);
			} catch (PuiDaoSaveException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void executeProcess(String name, Runnable runnable) {
		IPuiMultiInstanceProcess pmip;
		try {
			pmip = multiInstanceProcessDao.findOne(new PuiMultiInstanceProcessPk(name));
		} catch (PuiDaoFindException e) {
			pmip = null;
		}

		if (pmip == null) {
			logger.error("No multi instance process found for ID '" + name + "'");
			return;
		}

		if (!pmip.getInstanceassigneeuuid().equals(uuid)) {
			return;
		}

		try {
			pmip.setLatestexecution(Instant.now());

			multiInstanceProcessDao.patch(pmip.createPk(), Collections
					.singletonMap(IPuiMultiInstanceProcess.LATEST_EXECUTION_COLUMN, pmip.getLatestexecution()));
		} catch (PuiDaoSaveException e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("Executing background process '" + name + "' in instance " + uuid);
		runnable.run();
	}

}