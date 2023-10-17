package es.prodevelop.pui9.model.dao.elasticsearch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.InfoResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Component
@PropertySource(value = "classpath:elastic.properties", ignoreResourceNotFound = true)
public class PuiElasticSearchManager {

	private static final String URL_PREFIX = "jdbc:es://";
	private static final String URL_FULL_PREFIX = "jdbc:elasticsearch://";
	private static final URI DEFAULT_URI = URI.create("http://localhost:9200/");

	private static final String DATASOURCE_URL_FIELDNAME = "url";
	private static final String DATASOURCE_CONNECTION_STRING_FIELDNAME = "connectionString";
	private static final String DATASOURCE_USERNAME_FIELDNAME = "userName";
	private static final String DATASOURCE_PASSWORD_FIELDNAME = "password";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired(required = false)
	@Qualifier("elasticsearchJndiName")
	private String elasticsearchJndiName;

	@Value("${elastic.url}")
	private String url = null;

	@Value("${elastic.username}")
	private String username = null;

	@Value("${elastic.password}")
	private volatile String password = null;

	private ElasticsearchClient client;

	@PostConstruct
	private void postConstruct() {
		fillManager();
	}

	private void fillManager() {
		if (!ObjectUtils.isEmpty(elasticsearchJndiName)) {
			DataSource ds;
			try {
				JndiTemplate jndi = new JndiTemplate();
				ds = (DataSource) jndi.lookup(elasticsearchJndiName);
			} catch (NamingException e) {
				throw new IllegalArgumentException(e.getMessage());
			}

			url = getFieldValue(ds, DATASOURCE_URL_FIELDNAME);
			if (url == null) {
				url = getFieldValue(ds, DATASOURCE_CONNECTION_STRING_FIELDNAME);
			}
			username = getFieldValue(ds, DATASOURCE_USERNAME_FIELDNAME);
			password = getFieldValue(ds, DATASOURCE_PASSWORD_FIELDNAME);
		}
	}

	public ElasticsearchClient getClient() {
		synchronized (this) {
			if (client == null) {
				createClient();
			}

			if (!isConnected()) {
				reconnect();
			}

			return client;
		}
	}

	public void reconnect() {
		synchronized (this) {
			preDestroy();
			createClient();
		}
	}

	public boolean isConnected() {
		if (client == null) {
			return false;
		}

		try {
			return client.ping().value();
		} catch (IOException | RuntimeException e) {
			logger.warn("Elastic Search not connected: " + e.getMessage());
			return false;
		}
	}

	private void createClient() {
		if (client != null) {
			return;
		}

		try {
			URI uri = createURI(url);

			String scheme = uri.getScheme();
			String host = uri.getHost();
			Integer port = uri.getPort();

			RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, scheme))
					.setRequestConfigCallback(rcb -> rcb.setConnectTimeout(2000).setSocketTimeout(10000));

			if (!ObjectUtils.isEmpty(username) && !ObjectUtils.isEmpty(password)) {
				CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
				builder.setHttpClientConfigCallback(
						httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
			}

			RestClient restClient = builder.build();

			ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
			client = new ElasticsearchClient(transport);
			configureClient();

			if (isConnected()) {
				InfoResponse resp = client.info();
				logger.info("Connected to Elastic Search:");
				logger.info("\tCluster name: " + resp.clusterName());
				logger.info("\tCluster id: " + resp.clusterUuid());
				logger.info("\tNode name: " + resp.name());
				logger.info("\tElasticSearch version: " + resp.version().number());
			} else {
				logger.warn("Elastic Search is available but not connected to any node");
			}
		} catch (Exception e) {
			logger.warn("Elastic Search is not available: " + e.getMessage());
		}
	}

	private void configureClient() {
		((JacksonJsonpMapper) client._jsonpMapper()).objectMapper().registerModule(new JavaTimeModule())
				.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	private URI createURI(String url) {
		if (!canAcceptUrl(url)) {
			throw new IllegalArgumentException(
					"Expected [" + URL_PREFIX + "] or [" + URL_FULL_PREFIX + "] url, received [" + url + "]");
		}

		try {
			url = removeJdbcPrefix(url);
			return buildURI(url);
		} catch (IllegalArgumentException ex) {
			final String format = "jdbc:[es|elasticsearch]://[[http|https]://]?[host[:port]]?/[prefix]?[\\?[option=value]&]*";
			throw new IllegalArgumentException(
					"Invalid URL: " + ex.getMessage() + "; format should be [" + format + "]", ex);
		}
	}

	private boolean canAcceptUrl(String url) {
		String u = url.trim();
		return (StringUtils.hasText(u) && (u.startsWith(URL_PREFIX) || u.startsWith(URL_FULL_PREFIX)));
	}

	private String removeJdbcPrefix(String url) {
		if (url.startsWith(URL_PREFIX)) {
			return url.substring(URL_PREFIX.length());
		} else if (url.startsWith(URL_FULL_PREFIX)) {
			return url.substring(URL_FULL_PREFIX.length());
		} else {
			throw new IllegalArgumentException(
					"Expected [" + URL_PREFIX + "] or [" + URL_FULL_PREFIX + "] url, received [" + url + "]");
		}
	}

	private URI buildURI(String url) {
		URI innerUri = parseMaybeWithScheme(url);
		String scheme = (innerUri.getScheme() != null) ? innerUri.getScheme() : DEFAULT_URI.getScheme();
		String host = (innerUri.getHost() != null) ? innerUri.getHost() : DEFAULT_URI.getHost();
		String path = "".equals(innerUri.getPath()) ? DEFAULT_URI.getPath() : innerUri.getPath();
		String rawQuery = (innerUri.getQuery() == null) ? DEFAULT_URI.getRawQuery() : innerUri.getRawQuery();
		String rawFragment = (innerUri.getFragment() == null) ? DEFAULT_URI.getRawFragment()
				: innerUri.getRawFragment();
		int port = (innerUri.getPort() < 0) ? DEFAULT_URI.getPort() : innerUri.getPort();
		try {
			String connStr = (new URI(scheme, innerUri.getUserInfo(), host, port, path, null, null)).toString();
			if (StringUtils.hasLength(rawQuery))
				connStr = connStr + "?" + rawQuery;
			if (StringUtils.hasLength(rawFragment))
				connStr = connStr + "#" + rawFragment;
			return new URI(connStr);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid connection configuration [" + url + "]: " + e.getMessage(), e);
		}
	}

	private URI parseMaybeWithScheme(String url) {
		url = url.toLowerCase(Locale.ROOT);
		boolean hasAnHttpPrefix = (url.startsWith("http://") || url.startsWith("https://"));
		if (!hasAnHttpPrefix) {
			throw new IllegalArgumentException("No http or https scheme defined in the URL: " + url);
		}

		try {
			return new URI(url);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid connection configuration: " + e.getMessage(), e);
		}
	}

	private String getFieldValue(DataSource dataSource, String fieldname) {
		Field field;
		try {
			field = dataSource.getClass().getDeclaredField(fieldname);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
		field.setAccessible(true);

		try {
			return (String) field.get(dataSource);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	@PreDestroy
	private void preDestroy() {
		if (client == null) {
			return;
		}

		try {
			client._transport().close();
		} catch (IOException e) {
			// do nothing
		} finally {
			client = null;
			logger.info("Disconnected from Elastic Search:");
		}
	}

}
