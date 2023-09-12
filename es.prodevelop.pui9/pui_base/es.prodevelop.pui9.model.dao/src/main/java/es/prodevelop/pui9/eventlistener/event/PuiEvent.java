package es.prodevelop.pui9.eventlistener.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEvent;

import es.prodevelop.pui9.eventlistener.PuiEventLauncher;

/**
 * Abstract implementation for the Spring {@link ApplicationEvent}. Each event
 * is defined by:<br>
 * <ul>
 * <li><b>the Source</b>: the event object that will be passed to each listener
 * that listens to this event</li>
 * </ul>
 * <br>
 * The events are fired using the {@link PuiEventLauncher} class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiEvent<T> extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	protected final transient Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * Create a new Event with the given parameters
	 * 
	 * @param source  The object that provoikes the event to be fired
	 * @param eventId An identifier for the event
	 */
	protected PuiEvent(T source) {
		super(source);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getSource() {
		return (T) super.getSource();
	}

}
