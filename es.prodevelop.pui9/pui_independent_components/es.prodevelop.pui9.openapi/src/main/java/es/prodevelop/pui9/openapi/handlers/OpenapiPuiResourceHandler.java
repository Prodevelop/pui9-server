package es.prodevelop.pui9.openapi.handlers;

import static org.springdoc.core.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springdoc.core.Constants.SWAGGER_UI_PREFIX;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

import java.util.Optional;

import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import es.prodevelop.pui9.spring.configuration.IPuiResourceHandler;

@Component
public class OpenapiPuiResourceHandler implements IPuiResourceHandler {

	@Autowired
	private SwaggerIndexTransformer swaggerIndexTransformer;

	@Autowired
	private SwaggerUiConfigProperties swaggerUiConfigProperties;

	@Autowired
	private Optional<ActuatorProvider> actuatorProvider;

	@Override
	public void addResourceHandler(ResourceHandlerRegistry registry) {
		String swaggerPath = swaggerUiConfigProperties.getPath();

		StringBuilder uiRootPath = new StringBuilder();
		if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR)) {
			uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		}
		if (actuatorProvider.isPresent() && actuatorProvider.get().isUseManagementPort()) {
			uiRootPath.append(actuatorProvider.get().getBasePath());
		}

		registry.addResourceHandler(uiRootPath + SWAGGER_UI_PREFIX + "*/**")
				.addResourceLocations(
						CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.resourceChain(false).addTransformer(swaggerIndexTransformer);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/swagger").setViewName("redirect:" + "/swagger-ui");
		registry.addViewController("/swagger-ui").setViewName("redirect:" + "/swagger-ui/");
		registry.addViewController("/swagger-ui/").setViewName("forward:" + "/swagger-ui/index.html");
	}

}
