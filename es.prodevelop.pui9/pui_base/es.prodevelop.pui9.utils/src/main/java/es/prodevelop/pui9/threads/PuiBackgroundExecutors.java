package es.prodevelop.pui9.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * This singleton class helps to create backbround executors that will perform
 * actions periodically.
 * <p>
 * An executor could be defined as daemon (daemon thread) or not, depending on
 * the action to be performed.
 * <p>
 * For instance, if it's a executor that only refreshes a cache, this can be
 * defined as daemon thread.
 * <p>
 * In other hand, if you want to execute some task that it's important to be
 * finished, it will not be declared as daemon (but as user thread)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiBackgroundExecutors {

	private static PuiBackgroundExecutors instance;

	public static PuiBackgroundExecutors getSingleton() {
		if (instance == null) {
			instance = new PuiBackgroundExecutors();
		}
		return instance;
	}

	private final Logger logger = LogManager.getLogger(this.getClass());
	private List<ScheduledExecutorService> executors = new ArrayList<>();

	private PuiBackgroundExecutors() {
	}

	/**
	 * Registers a new executor that will be executed at the defined fixed delay
	 * (what means that subsequent executions will take part in the 'delay' timeUnit
	 * after the completion of the previous execution)
	 * 
	 * @param name         The name of the Executor that will be used for the
	 *                     created Thread
	 * @param isDaemon     If it's a Daemon or not
	 * @param initialDelay The initial execution delay before the first execution
	 * @param delay        The delay between subsequent executions
	 * @param unit         The unit of the delays
	 * @param runnable     The runnable to be executed
	 */
	public void registerNewExecutor(String name, boolean isDaemon, long initialDelay, long delay, TimeUnit unit,
			Runnable runnable) {
		ScheduledExecutorService ses = buildExecutorService(name, isDaemon, unit);
		ses.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
	}

	/**
	 * Registers a new executor that will be executed at the defined fixed rate
	 * (what means that subsequent executions will take part in the fixed 'delay'
	 * timeUnit after the start of the previous execution)
	 * 
	 * @param name         The name of the Executor that will be used for the
	 *                     created Thread
	 * @param isDaemon     If it's a Daemon or not
	 * @param initialDelay The initial execution delay before the first execution
	 * @param delay        The delay between subsequent executions
	 * @param unit         The unit of the delays
	 * @param runnable     The runnable to be executed
	 */
	public void registerNewExecutorAtFixedRate(String name, boolean isDaemon, long initialDelay, long delay,
			TimeUnit unit, Runnable runnable) {
		ScheduledExecutorService ses = buildExecutorService(name, isDaemon, unit);
		ses.scheduleAtFixedRate(runnable, initialDelay, delay, unit);
	}

	private ScheduledExecutorService buildExecutorService(String name, boolean isDaemon, TimeUnit unit) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Empty executor name");
		}
		if (unit == null) {
			throw new IllegalArgumentException("No time unit for executor");
		}

		ThreadFactory tf = new ThreadFactoryBuilder().setDaemon(isDaemon).setNameFormat("PuiExecutor_" + name)
				.setUncaughtExceptionHandler(
						(thread, throwable) -> logger.error("Error in Background Executor '" + name + "'", throwable))
				.build();
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor(tf);
		executors.add(ses);

		return ses;
	}

	public void destroy() {
		for (ScheduledExecutorService ses : executors) {
			ses.shutdownNow();
		}
	}

}
