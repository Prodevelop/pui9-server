package es.prodevelop.pui9.spring.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springdoc.core.SpringDocConfigProperties.ApiDocs.OpenApiVersion;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerMapping;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiExceptionDto;
import es.prodevelop.pui9.messages.PuiMessagesRegistry;
import es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiObjectUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.Parameter.StyleEnum;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@PuiSpringConfiguration
@PropertySource(value = "classpath:openapi_config.properties")
@PropertySource(value = "classpath:openapi.properties", ignoreResourceNotFound = true)
@ComponentScan({ "org.springdoc" })
@Import(PropertyPlaceholderAutoConfiguration.class)
public class PuiOpenapiSpringConfiguration {

	public static final String AUTH_BEARER_JWT = "Bearer JWT";
	public static final String AUTH_USER_PASSWORD = "User-Password";
	public static final String AUTH_API_KEY = "Api-Key";

	@Value(PuiOpenapiConstants.BASE_URL)
	private String baseUrl;

	@Value(PuiOpenapiConstants.INFO_TITLE)
	private String infoTitle;

	@Value(PuiOpenapiConstants.INFO_DESCRIPTION)
	private String infoDescription;

	@Value(PuiOpenapiConstants.INFO_VERSION)
	private String infoVersion;

	@Value(PuiOpenapiConstants.INFO_TERMS_OF_SERVICE_URL)
	private String infoTermsOfServiceUrl;

	@Value(PuiOpenapiConstants.INFO_LICENSE)
	private String license;

	@Value(PuiOpenapiConstants.INFO_LICENSE_URL)
	private String licenseUrl;

	@Value(PuiOpenapiConstants.CONTACT_NAME)
	private String contactName;

	@Value(PuiOpenapiConstants.CONTACT_EMAIL)
	private String contactEmail;

	@Value(PuiOpenapiConstants.CONTACT_URL)
	private String contactUrl;

	@Autowired
	private PuiRequestMappingHandlerMapping requestMapping;

	@Autowired
	private ServletContext servletContext;

	private Map<String, HandlerMethod> pathMethodMap;
	private Map<Class<?>, Pair<String, ApiResponse>> mapExceptions;
	private boolean apiCompleted = false;

	@Bean
	public OpenAPI openApi() {
		Json.mapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

		return new OpenAPI().addServersItem(server()).components(components()).info(info());
	}

	@Bean
	public OpenApiCustomiser consumerTypeHeaderOpenAPICustomiser() {
		Parameter timezoneParameter = new Parameter().$ref(PuiConstants.HEADER_TIMEZONE);
		Parameter sourceParameter = new Parameter().$ref(PuiConstants.HEADER_SOURCE);

		return openApi -> {
			if (apiCompleted) {
				return;
			}

			pathMethodMap = new LinkedHashMap<>();
			mapExceptions = new LinkedHashMap<>();

			Set<String> securedPredicates = new LinkedHashSet<>();
			Set<String> apiKeyPredicates = new LinkedHashSet<>();
			requestMapping.getHandlerMethods().forEach((info, handlerMethod) -> {
				String path = info.getPatternsCondition().getPatterns().iterator().next();
				pathMethodMap.put(path, handlerMethod);
				if (requestMapping.isWebServiceSessionRequired(handlerMethod)) {
					securedPredicates.add(path);
				}
				if (requestMapping.isWebServiceApiKeySupported(handlerMethod)) {
					apiKeyPredicates.add(path);
				}
			});

			if (!ObjectUtils.isEmpty(securedPredicates)) {
				openApi.getComponents().addSecuritySchemes(AUTH_BEARER_JWT, securitySchemeBearer());
				openApi.getComponents().addSecuritySchemes(AUTH_USER_PASSWORD, securitySchemeBasic());
			}
			if (!ObjectUtils.isEmpty(apiKeyPredicates)) {
				openApi.getComponents().addSecuritySchemes(AUTH_API_KEY, securitySchemeApiKey());
			}

			openApi.getPaths().forEach((path, item) -> item.readOperations().forEach(operation -> {
				operation.addParametersItem(timezoneParameter);
				operation.addParametersItem(sourceParameter);
				if (securedPredicates.contains(path)) {
					operation.addSecurityItem(new SecurityRequirement().addList(AUTH_BEARER_JWT));
					operation.addSecurityItem(new SecurityRequirement().addList(AUTH_USER_PASSWORD));
				}
				if (apiKeyPredicates.contains(path)) {
					operation.addSecurityItem(new SecurityRequirement().addList(AUTH_API_KEY));
				}

				buildExceptions(openApi, path, operation);
			}));

			pathMethodMap.clear();
			mapExceptions.clear();
			apiCompleted = true;
		};
	}

	@Bean
	public SwaggerUiConfigProperties swaggerUiConfigProperties() {
		SwaggerUiConfigProperties props = new SwaggerUiConfigProperties();
		props.setDisplayRequestDuration(true);
		props.setDocExpansion("none");
		props.setDefaultModelExpandDepth(0);
		props.setDefaultModelsExpandDepth(0);
		props.setDisableSwaggerDefaultUrl(true);
		props.setEnabled(true);
		props.setPath("/swagger-ui.html");
		props.setVersion(OpenApiVersion.OPENAPI_3_1.getVersion());

		if (ObjectUtils.isEmpty(baseUrl)) {
			baseUrl = servletContext.getContextPath();
		}

		props.setConfigUrl(baseUrl + "/api-docs/swagger-config");
		props.setUrl(baseUrl + "/api-docs");
		props.setOperationsSorter("alpha");
		props.setTagsSorter("alpha");

		return props;
	}

	private Server server() {
		String defaultBaseUrl = servletContext.getContextPath();
		if (!ObjectUtils.isEmpty(baseUrl)) {
			defaultBaseUrl = baseUrl;
		}
		return new Server().url(defaultBaseUrl).description("Default server");
	}

	private Components components() {
		return new Components().addParameters(PuiConstants.HEADER_TIMEZONE, timezoneParameter())
				.addParameters(PuiConstants.HEADER_SOURCE, sourceParameter()).addSchemas("class", fakeClassSchema());
	}

	private SecurityScheme securitySchemeBasic() {
		return new SecurityScheme().type(Type.HTTP).scheme("basic");
	}

	private SecurityScheme securitySchemeBearer() {
		return new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT");
	}

	private SecurityScheme securitySchemeApiKey() {
		return new SecurityScheme().type(Type.APIKEY).name(PuiConstants.HEADER_API_KEY).in(In.HEADER);
	}

	private Parameter timezoneParameter() {
		List<String> timezoneList = new ArrayList<>();
		timezoneList.add(ZoneId.systemDefault().getId());
		timezoneList.add("Europe/Madrid");

		return new Parameter().in("header").schema(new StringSchema()._enum(timezoneList)._default(timezoneList.get(0)))
				.name(PuiConstants.HEADER_TIMEZONE).allowEmptyValue(false).required(true).style(StyleEnum.SIMPLE);
	}

	private Parameter sourceParameter() {
		List<String> sourceList = new ArrayList<>();
		sourceList.add("openapi");

		return new Parameter().in("header").schema(new StringSchema()._enum(sourceList)._default(sourceList.get(0)))
				.name(PuiConstants.HEADER_SOURCE).allowEmptyValue(false).required(true).style(StyleEnum.SIMPLE);
	}

	private Schema<?> fakeClassSchema() {
		return new StringSchema();
	}

	private Info info() {
		return new Info().title(infoTitle).description(infoDescription).version(infoVersion)
				.termsOfService(infoTermsOfServiceUrl).contact(contact()).license(license());
	}

	private Contact contact() {
		return new Contact().name(contactName).url(contactUrl).email(contactEmail);
	}

	private License license() {
		return new License().name(license).url(licenseUrl);
	}

	@SuppressWarnings("rawtypes")
	private void buildExceptions(OpenAPI openApi, String path, Operation operation) {
		HandlerMethod hm = pathMethodMap.get(path);
		if (hm == null) {
			return;
		}

		Map<String, Schema> exceptionDtoProperties = new LinkedHashMap<>();
		PuiObjectUtils.getFields(PuiExceptionDto.class).forEach((name, field) -> {
			Schema<?> propSchema = new Schema<>().type(field.getType().getSimpleName().toLowerCase());
			if (Modifier.isStatic(field.getModifiers())) {
				return;
			}
			if (field.getType().equals(Instant.class)) {
				propSchema.type(String.class.getSimpleName().toLowerCase()).format("date-time");
			}
			exceptionDtoProperties.put(name, propSchema);
		});

		for (Class<?> exClass : hm.getMethod().getExceptionTypes()) {
			if (!mapExceptions.containsKey(exClass)) {
				Integer code = -1;
				String description;
				try {
					Field codeField = exClass.getDeclaredField("CODE");
					code = (Integer) codeField.get(exClass);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e) {
					code = PuiException.DEFAULT_INTERNAL_CODE;
				}

				description = PuiMessagesRegistry.getSingleton().getString(PuiLanguageUtils.getDefaultLanguage(),
						code.toString());

				PuiException ex;
				try {
					Constructor<?> constructor = exClass.getConstructors()[0];
					Object[] params = new Object[constructor.getParameterTypes().length];
					ex = (PuiException) constructor.newInstance(params);
				} catch (Exception e) {
					continue;
				}

				PuiExceptionDto exDto = ex.asTransferObject();
				exDto.setClassName(exClass.getSimpleName());
				exDto.setDetailedMessage("Detailed message, if applies");
				exDto.setErrorClassName("Class name where the error was thrown");
				exDto.setUrl(path);
				exDto.setMessage(description != null ? description : ex.getMessage());
				exDto.setInternalCode(code);

				ApiResponse ar = new ApiResponse().description(description).content(new Content().addMediaType(
						org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
						new MediaType().schema(new ObjectSchema().example(exDto).properties(exceptionDtoProperties))));
				Pair<String, ApiResponse> pair = new ImmutablePair<>(
						exDto.getStatusCode() + " (" + code.toString() + ")", ar);
				mapExceptions.put(exClass, pair);
				openApi.getComponents().addResponses(pair.getKey(), ar);
			}

			String code = mapExceptions.get(exClass).getKey();
			if (!ObjectUtils.isEmpty(code)) {
				operation.getResponses().addApiResponse(code, new ApiResponse().$ref(code));
			}
		}
	}

}