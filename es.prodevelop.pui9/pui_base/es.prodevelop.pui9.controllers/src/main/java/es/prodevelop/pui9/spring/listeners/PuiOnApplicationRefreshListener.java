package es.prodevelop.pui9.spring.listeners;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.messages.AbstractPuiMessages;
import es.prodevelop.pui9.spring.configuration.AbstractAppSpringConfiguration;

@Component
public class PuiOnApplicationRefreshListener {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private AbstractAppSpringConfiguration appConfig;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!event.getApplicationContext().equals(PuiApplicationContext.getInstance().getAppContext())) {
			return;
		}

		loadMessages();

		logger.info("PUI9 Application context refresh finished");
		logger.debug(
				"You can register an *EventListener* for *ContextRefreshedEvent* to perform some initial actions on your application");
	}

	private void loadMessages() {
		// obtain all the packages to scan
		List<String> packageScan = new ArrayList<>();
		packageScan.add("es.prodevelop");
		ComponentScan componentScan = ClassUtils.getUserClass(appConfig).getAnnotation(ComponentScan.class);
		if (componentScan != null) {
			packageScan.addAll(Arrays.asList(componentScan.basePackages()));
		}

		// force to load all the messages of the application
		new Reflections(packageScan.toArray()).getSubTypesOf(AbstractPuiMessages.class).forEach(pm -> {
			try {
				pm.getMethod(AbstractPuiMessages.GET_SINGLETON_METHOD_NAME).invoke(null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// do nothing
			}
		});
	}

}