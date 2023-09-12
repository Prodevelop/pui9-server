package es.prodevelop.pui9.utils;

/**
 * Utility class to configure a new external call for {@link PuiExternalRequest}.
 * Use the builder() method to build a new instance 
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

public class PuiExternalRequestConfig {

	public static final int DEFAULT_TIMEOUT = 10000;

	public static PuiExternalRequestConfig builder() {
		return new PuiExternalRequestConfig();
	}

	private String url;
	private Class<?> returnClass;
	private ParameterizedTypeReference<?> returnParameterizedType;
	private MultiValueMap<String, String> params;
	private Object body;
	private HttpHeaders headers;
	private Integer timeout = DEFAULT_TIMEOUT;
	private MediaType contentType = MediaType.APPLICATION_JSON;

	private PuiExternalRequestConfig() {
	}

	/**
	 * URL of the Web Service
	 * 
	 * @param url URL of the Web Wervice
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * If the Web Service returns a known class not parameterized
	 * 
	 * @param returnClass The returning object class
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withReturnClass(Class<?> returnClass) {
		this.returnClass = returnClass;
		return this;
	}

	/**
	 * If the Web Service returns a parameterized class
	 * 
	 * @param returnParameterizedType The returning parameterized object class
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withReturnParameterizedType(ParameterizedTypeReference<?> returnParameterizedType) {
		this.returnParameterizedType = returnParameterizedType;
		return this;
	}

	/**
	 * The parameters added to the URL
	 * 
	 * @param params The parameters
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withParameters(MultiValueMap<String, String> params) {
		this.params = params;
		return this;
	}

	/**
	 * The body for a POST request
	 * 
	 * @param body The object of the POST
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withBody(Object body) {
		this.body = body;
		return this;
	}

	/**
	 * Use these headers in the request
	 * 
	 * @param headers The headers of the request
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withHeaders(HttpHeaders headers) {
		this.headers = headers;
		return this;
	}

	/**
	 * Set the content type for the request
	 * 
	 * @param contentType The content type
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withContentType(MediaType contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Time in Milliseconds
	 * 
	 * @param timeout time in milliseconds
	 * @return The configuration object
	 */
	public PuiExternalRequestConfig withTimeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Class<?> getReturnClass() {
		return returnClass;
	}

	public ParameterizedTypeReference<?> getReturnParameterizedType() {
		return returnParameterizedType;
	}

	public MultiValueMap<String, String> getParams() {
		return params;
	}

	public Object getBody() {
		return body;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public MediaType getContentType() {
		return contentType;
	}

}
