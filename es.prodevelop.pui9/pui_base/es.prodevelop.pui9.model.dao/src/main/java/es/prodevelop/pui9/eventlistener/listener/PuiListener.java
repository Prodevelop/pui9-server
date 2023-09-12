package es.prodevelop.pui9.eventlistener.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Abstract implementation for the Spring {@link ApplicationListener}. The final
 * implementations of this class, should be defined with the {@link Component}
 * annotation in order to create the bean automatically.<br>
 * <br>
 * It's possible to specify a filter for this Listener using the
 * {@link #passFilter(IDto)} method. This way, you can filter the execution of
 * this listener based on some checks over the affected DTO
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiListener<E extends PuiEvent<?>> implements ApplicationListener<E> {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public final void onApplicationEvent(E event) {
		if (passFilter(event)) {
			try {
				process(event);
			} catch (PuiException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Check if the current Listener is executable to the given DTO. By default,
	 * returns true
	 * 
	 * @param event The whole event object
	 * @return true if the listener should be processed; false if not
	 */
	protected boolean passFilter(E event) {
		return true;
	}

	/**
	 * Process the listener with the given {@link PuiEvent} event
	 * 
	 * @param event The event
	 * @throws PuiException If any exception occurs while processing the Event
	 */
	protected abstract void process(E event) throws PuiException;

}
