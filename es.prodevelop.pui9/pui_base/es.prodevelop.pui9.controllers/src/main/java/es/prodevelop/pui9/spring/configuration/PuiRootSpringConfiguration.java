package es.prodevelop.pui9.spring.configuration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.pui9.PuiExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.pui9.PuiIDtoArgumentResolver;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import es.prodevelop.pui9.data.converters.DtoConverter;
import es.prodevelop.pui9.data.converters.InstantConverter;
import es.prodevelop.pui9.data.converters.LocalDateConverter;
import es.prodevelop.pui9.data.converters.MapConverter;
import es.prodevelop.pui9.data.converters.MultipartFileConverter;
import es.prodevelop.pui9.data.converters.PuiGsonHttpMessageConverter;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

/**
 * This is the Root Configuration for all the PUI applications. Contains all the
 * common shared configuration for all of them. This configuration is loaded
 * after the concrete configuration of the application, in the
 * {@link PuiWebApplicationSpringConfiguration}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
@ComponentScan(basePackages = { "es.prodevelop.pui9" })
public class PuiRootSpringConfiguration extends WebMvcConfigurationSupport {

	@Autowired
	private AbstractAppSpringConfiguration appConfig;

	@Autowired(required = false)
	private Optional<List<IPuiResourceHandler>> resourceHandlers;

	private CommonsMultipartResolver resolver = new CommonsMultipartResolver();

	/**
	 * Don't remove this method override, because if you do it, the PUI applications
	 * initialization will fail
	 */
	@Bean
	@Override
	public PuiRequestMappingHandlerMapping requestMappingHandlerMapping(
			@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
			@Qualifier("mvcConversionService") FormattingConversionService conversionService,
			@Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider) {
		return (PuiRequestMappingHandlerMapping) super.requestMappingHandlerMapping(contentNegotiationManager,
				conversionService, resourceUrlProvider);
	}

	/**
	 * Don't remove this method override, because if you do it, the PUI applications
	 * initialization will fail
	 */
	@Bean
	@Override
	public PuiRequestMappingHandlerAdapter requestMappingHandlerAdapter(
			@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
			@Qualifier("mvcConversionService") FormattingConversionService conversionService,
			@Qualifier("mvcValidator") Validator validator) {
		return (PuiRequestMappingHandlerAdapter) super.requestMappingHandlerAdapter(contentNegotiationManager,
				conversionService, validator);
	}

	/**
	 * Creates a PUI extension of the {@link RequestMappingHandlerMapping} class
	 */
	@Override
	protected PuiRequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		return new PuiRequestMappingHandlerMapping();
	}

	/**
	 * Creates a PUI extension of the {@link RequestMappingHandlerAdapter} class
	 */
	@Override
	protected PuiRequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
		return new PuiRequestMappingHandlerAdapter(getMessageConverters());
	}

	/**
	 * Creates a PUI extension of the {@link ExceptionHandlerExceptionResolver}
	 * class
	 */
	@Override
	protected PuiExceptionHandlerExceptionResolver createExceptionHandlerExceptionResolver() {
		return new PuiExceptionHandlerExceptionResolver(getMessageConverters());
	}

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PuiIDtoArgumentResolver());
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(appConfig.actionInterceptor()).addPathPatterns("/**");
	}

	/**
	 * Substitute the {@link PuiGsonHttpMessageConverter} and the
	 * {@link FormHttpMessageConverter} from GSON default by the PUI ones
	 */
	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0, new PuiGsonHttpMessageConverter());
	}

	/**
	 * Add the PUI Generic Converters
	 */
	@Override
	protected void addFormatters(FormatterRegistry registry) {
		for (GenericConverter converter : getPuiGenericConverters()) {
			registry.addConverter(converter);
		}
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (resourceHandlers.isPresent()) {
			resourceHandlers.get().forEach(rh -> rh.addResourceHandler(registry));
		}
	}

	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
		if (resourceHandlers.isPresent()) {
			resourceHandlers.get().forEach(rh -> rh.addViewControllers(registry));
		}
	}

	/**
	 * This method creates a bean of type CommonsMultipartResoler (with name
	 * 'multipartResolver'), that is used for uploading files to the server.
	 * Encoding is set to UTF-8
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		resolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
		resolver.setMaxUploadSizePerFile(20 * 1024 * 1024L);
		return resolver;
	}

	/**
	 * This method creates a bean of type ConvertionServiceFactoryBean (with name
	 * 'conversionService'), necessary to look for all the "beanable" classes. Don't
	 * delete it
	 */
	@Bean
	public ConversionServiceFactoryBean conversionService() {
		Set<GenericConverter> converters = new HashSet<>();
		converters.addAll(getPuiGenericConverters());

		ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
		conversionService.setConverters(converters);

		return conversionService;
	}

	/**
	 * Get all the PUI Generic Converters
	 * 
	 * @return list of all the own Generic Converters
	 */
	private List<GenericConverter> getPuiGenericConverters() {
		List<GenericConverter> converters = new ArrayList<>();

		converters.add(new DtoConverter());
		converters.add(new InstantConverter());
		converters.add(new LocalDateConverter());
		converters.add(new MapConverter());
		converters.add(new MultipartFileConverter());

		return converters;
	}

}
