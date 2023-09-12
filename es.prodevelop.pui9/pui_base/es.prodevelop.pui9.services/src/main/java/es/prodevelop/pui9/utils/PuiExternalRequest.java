package es.prodevelop.pui9.utils;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Collections;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;
import org.springframework.web.util.UriComponentsBuilder;

import es.prodevelop.pui9.data.converters.PuiGsonHttpMessageConverter;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiExceptionDto;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.services.exceptions.PuiServiceTimeoutException;

/**
 * Utility class to make external request from PUI server to anywhere. POST and
 * GET methods are accepted. It uses the {@link RestTemplate} object of Spring
 * to make the requests. User {@link PuiExternalRequestConfig} to configure an
 * external call
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiExternalRequest {

	private static PuiExternalRequest singleton;

	public static PuiExternalRequest getSingleton() {
		if (singleton == null) {
			singleton = new PuiExternalRequest();
		}
		return singleton;
	}

	/**
	 * Configure the {@link RestTemplate} object with the valid converters for PUI
	 */
	private PuiExternalRequest() {
	}

	private RestTemplate createRestTemplate(PuiExternalRequestConfig config) {
		RestTemplate restTemplate = new RestTemplate();

		if (restTemplate.getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
			configureTemplateSetTimeouts(restTemplate, config.getTimeout());
		}

		configureTemplateSetConverters(restTemplate);
		configureTemplateEncoding(restTemplate);

		return restTemplate;
	}

	private HttpHeaders createHttpHeaders(PuiExternalRequestConfig config) {
		HttpHeaders baseHeaders = new HttpHeaders();
		baseHeaders.setAccept(Collections.singletonList(config.getContentType()));
		baseHeaders.setContentType(config.getContentType());

		HttpHeaders newHeaders = config.getHeaders() != null ? config.getHeaders() : new HttpHeaders();
		baseHeaders.forEach(newHeaders::putIfAbsent);
		newHeaders.putIfAbsent(HttpHeaders.ACCEPT_LANGUAGE,
				Collections.singletonList(PuiUserSession.getSessionLanguage().getIsocode()));

		return newHeaders;
	}

	/**
	 * Change the timeout of the request. By default, 10000 milliseconds are set if
	 * the milliseconds parameter is null
	 * 
	 * @param milliseconds
	 */
	private void configureTemplateSetTimeouts(RestTemplate restTemplate, Integer milliseconds) {
		if (milliseconds == null) {
			milliseconds = PuiExternalRequestConfig.DEFAULT_TIMEOUT;
		}
		SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		factory.setReadTimeout(milliseconds);
		factory.setConnectTimeout(milliseconds);
	}

	private void configureTemplateSetConverters(RestTemplate restTemplate) {
		restTemplate.getMessageConverters().add(0, new PuiGsonHttpMessageConverter());
	}

	private void configureTemplateEncoding(RestTemplate restTemplate) {
		DefaultUriBuilderFactory uriFactory = new DefaultUriBuilderFactory();
		uriFactory.setEncodingMode(EncodingMode.VALUES_ONLY);
		restTemplate.setUriTemplateHandler(uriFactory);
	}

	/**
	 * Execute a GET request. This call is synchonous
	 * 
	 * @param <RETURN> The type of the response object
	 * @param config   The configuration object
	 * @return The response
	 * @throws PuiException If any exception occurs
	 */
	@SuppressWarnings("unchecked")
	public <RETURN> RETURN executeGet(PuiExternalRequestConfig config) throws PuiException {
		String url = config.getUrl();
		if (!ObjectUtils.isEmpty(config.getParams())) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(config.getParams()).toUriString();
		}

		RestTemplate restTemplate = createRestTemplate(config);
		HttpHeaders httpHeaders = createHttpHeaders(config);
		HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

		try {
			ResponseEntity<RETURN> response;
			if (config.getReturnClass() != null) {
				response = restTemplate.exchange(url, HttpMethod.GET, entity, (Class<RETURN>) config.getReturnClass());
			} else if (config.getReturnParameterizedType() != null) {
				response = restTemplate.exchange(url, HttpMethod.GET, entity,
						(ParameterizedTypeReference<RETURN>) config.getReturnParameterizedType());
			} else {
				response = restTemplate.exchange(url, HttpMethod.GET, entity, (Class<RETURN>) Void.class);
			}

			if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY)) {
				changeLocation(config, response);
				return executeGet(config);
			}

			return response.getBody();
		} catch (Exception e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a POST request. This call is synchonous.
	 * 
	 * @param <BODY>   The type of the body object
	 * @param <RETURN> The type of the response object
	 * @param config   The configuration object
	 * @return The response
	 * @throws PuiException If any exception occurs
	 */
	@SuppressWarnings("unchecked")
	public <BODY, RETURN> RETURN executePost(PuiExternalRequestConfig config) throws PuiException {
		String url = config.getUrl();
		if (!ObjectUtils.isEmpty(config.getParams())) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(config.getParams()).toUriString();
		}

		RestTemplate restTemplate = createRestTemplate(config);
		HttpHeaders httpHeaders = createHttpHeaders(config);
		HttpEntity<BODY> entity = new HttpEntity<>((BODY) config.getBody(), httpHeaders);

		try {
			ResponseEntity<RETURN> response;
			if (config.getReturnClass() != null) {
				response = restTemplate.postForEntity(url, entity, (Class<RETURN>) config.getReturnClass());
			} else if (config.getReturnParameterizedType() != null) {
				response = restTemplate.exchange(url, HttpMethod.POST, entity,
						(ParameterizedTypeReference<RETURN>) config.getReturnParameterizedType());
			} else {
				response = restTemplate.postForEntity(url, entity, (Class<RETURN>) Void.class);
			}

			if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY)) {
				changeLocation(config, response);
				return executePost(config);
			}

			return response.getBody();
		} catch (HttpServerErrorException e1) {
			String errorJson = e1.getResponseBodyAsString();
			PuiException e;
			try {
				PuiExceptionDto error = GsonSingleton.getSingleton().getGson().fromJson(errorJson,
						PuiExceptionDto.class);
				error.setStatusCode(e1.getStatusCode().value());
				error.setUrl(url);
				e = PuiException.fromTransferObject(error);
			} catch (Exception e2) {
				e = new PuiException(e1, e1.getStatusText());
				e.setStatusResponse(e1.getStatusCode().value());
			}
			throw e;
		} catch (HttpClientErrorException e1) {
			PuiException e = new PuiException(e1, e1.getStatusText());
			e.setStatusResponse(e1.getStatusCode().value());
			throw e;
		} catch (ResourceAccessException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new PuiServiceTimeoutException(config.getTimeout());
			} else {
				throw new PuiException(e);
			}
		} catch (Exception e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a PUT request. This call is synchonous.
	 * 
	 * @param <BODY>   The type of the body object
	 * @param <RETURN> The type of the response object
	 * @param config   The configuration object
	 * @return The response
	 * @throws PuiException If any exception occurs
	 */
	@SuppressWarnings("unchecked")
	public <BODY, RETURN> RETURN executePut(PuiExternalRequestConfig config) throws PuiException {
		String url = config.getUrl();
		if (!ObjectUtils.isEmpty(config.getParams())) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(config.getParams()).toUriString();
		}

		RestTemplate restTemplate = createRestTemplate(config);
		HttpHeaders httpHeaders = createHttpHeaders(config);
		HttpEntity<BODY> entity = new HttpEntity<>((BODY) config.getBody(), httpHeaders);

		try {
			ResponseEntity<RETURN> response;
			if (config.getReturnClass() != null) {
				response = restTemplate.exchange(url, HttpMethod.PUT, entity, (Class<RETURN>) config.getReturnClass());
			} else if (config.getReturnParameterizedType() != null) {
				response = restTemplate.exchange(url, HttpMethod.PUT, entity,
						(ParameterizedTypeReference<RETURN>) config.getReturnParameterizedType());
			} else {
				response = restTemplate.exchange(url, HttpMethod.PUT, entity, (Class<RETURN>) Void.class);
			}

			if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY)) {
				changeLocation(config, response);
				return executePut(config);
			}

			return response.getBody();
		} catch (HttpServerErrorException e1) {
			String errorJson = e1.getResponseBodyAsString();
			PuiException e;
			try {
				PuiExceptionDto error = GsonSingleton.getSingleton().getGson().fromJson(errorJson,
						PuiExceptionDto.class);
				error.setStatusCode(e1.getStatusCode().value());
				error.setUrl(url);
				e = PuiException.fromTransferObject(error);
			} catch (Exception e2) {
				e = new PuiException(e1, e1.getStatusText());
				e.setStatusResponse(e1.getStatusCode().value());
			}
			throw e;
		} catch (HttpClientErrorException e1) {
			PuiException e = new PuiException(e1, e1.getStatusText());
			e.setStatusResponse(e1.getStatusCode().value());
			throw e;
		} catch (ResourceAccessException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new PuiServiceTimeoutException(config.getTimeout());
			} else {
				throw new PuiException(e);
			}
		} catch (Exception e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a PATCH request. This call is synchonous. This call should include
	 * the PK as URL parameter, and an object set with the keys-values to be patched
	 * as body of the request
	 * 
	 * @param <BODY>   The type of the body object
	 * @param <RETURN> The type of the response object
	 * @param config   The configuration object
	 * @return The response
	 * @throws PuiException If any exception occurs
	 */
	@SuppressWarnings("unchecked")
	public <BODY, RETURN> RETURN executePatch(PuiExternalRequestConfig config) throws PuiException {
		String url = config.getUrl();
		if (!ObjectUtils.isEmpty(config.getParams())) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(config.getParams()).toUriString();
		}

		RestTemplate restTemplate = createRestTemplate(config);
		HttpHeaders httpHeaders = createHttpHeaders(config);
		HttpEntity<BODY> entity = new HttpEntity<>((BODY) config.getBody(), httpHeaders);

		try {
			ResponseEntity<RETURN> response;
			if (config.getReturnClass() != null) {
				response = restTemplate.exchange(url, HttpMethod.PATCH, entity,
						(Class<RETURN>) config.getReturnClass());
			} else if (config.getReturnParameterizedType() != null) {
				response = restTemplate.exchange(url, HttpMethod.PATCH, entity,
						(ParameterizedTypeReference<RETURN>) config.getReturnParameterizedType());
			} else {
				response = restTemplate.exchange(url, HttpMethod.PATCH, entity, (Class<RETURN>) Void.class);
			}

			if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY)) {
				changeLocation(config, response);
				return executePatch(config);
			}

			return response.getBody();
		} catch (HttpServerErrorException e1) {
			String errorJson = e1.getResponseBodyAsString();
			PuiException e;
			try {
				PuiExceptionDto error = GsonSingleton.getSingleton().getGson().fromJson(errorJson,
						PuiExceptionDto.class);
				error.setStatusCode(e1.getStatusCode().value());
				error.setUrl(url);
				e = PuiException.fromTransferObject(error);
			} catch (Exception e2) {
				e = new PuiException(e1, e1.getStatusText());
				e.setStatusResponse(e1.getStatusCode().value());
			}
			throw e;
		} catch (HttpClientErrorException e1) {
			PuiException e = new PuiException(e1, e1.getStatusText());
			e.setStatusResponse(e1.getStatusCode().value());
			throw e;
		} catch (ResourceAccessException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new PuiServiceTimeoutException(config.getTimeout());
			} else {
				throw new PuiException(e);
			}
		} catch (Exception e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a DELETE request. This call is synchonous.
	 * 
	 * @param config The configuration object
	 * @return The response
	 * @throws PuiException If any exception occurs
	 */
	public void executeDelete(PuiExternalRequestConfig config) throws PuiException {
		String url = config.getUrl();
		if (!ObjectUtils.isEmpty(config.getParams())) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(config.getParams()).toUriString();
		}

		RestTemplate restTemplate = createRestTemplate(config);
		HttpHeaders httpHeaders = createHttpHeaders(config);
		HttpEntity<Void> entity = new HttpEntity<>((Void) config.getBody(), httpHeaders);

		try {
			ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

			if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY)) {
				changeLocation(config, response);
				executeDelete(config);
			}

		} catch (HttpServerErrorException e) {
			throw new PuiException(e);
		}
	}

	private void changeLocation(PuiExternalRequestConfig config, ResponseEntity<?> response) throws PuiException {
		URI location = response.getHeaders().getLocation();
		if (location == null) {
			throw new PuiException("Moved permanently, but no location specified");
		}
		config.withUrl(location.toString().replace(location.getQuery(), ""));
	}

	public static void main(String[] args) {
		PuiExceptionDto error = new PuiExceptionDto();
		error.setMessage("");
		error.setStatusCode(200);
		error.setUrl("URL");
		PuiException.fromTransferObject(error);
	}

}
