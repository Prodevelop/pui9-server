package es.prodevelop.pui9.spring.configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * Subclass this interface if you want to provide some resources in the
 * application. Just by creating a subclass and implementing the
 * {@link #addResourceHandler(ResourceHandlerRegistry)} method, the resources
 * will be added
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiResourceHandler {

	default void addResourceHandler(ResourceHandlerRegistry registry) {

	}

	default void addViewControllers(ViewControllerRegistry registry) {

	}

}
