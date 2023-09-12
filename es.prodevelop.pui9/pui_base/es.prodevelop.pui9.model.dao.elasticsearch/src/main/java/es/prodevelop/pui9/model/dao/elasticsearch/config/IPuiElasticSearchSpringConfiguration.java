package es.prodevelop.pui9.model.dao.elasticsearch.config;

public interface IPuiElasticSearchSpringConfiguration {

	/**
	 * Mark this method as Bean. It returns the name of the Jndi for the Elastic
	 * Search connection.<br>
	 * Example of resource connection:<br>
	 * <br>
	 * <code>&lt;Resource name="jdbc/es.prodevelop.yourappname.elasticsearch"</code><br>
	 * <code>auth="Container" type="javax.sql.DataSource" username="" password=""</code><br>
	 * <code>url="jdbc:es://http://svlelastic01.prodevelop.es:9201" /&gt;</code>
	 * 
	 * @return The JNDI name for the Elastic Search connection
	 */
	String elasticsearchJndiName();

	/**
	 * Mark this method as Bean. It returns the name of the app used as prefix in
	 * the indices. Registries in the table PUI_ELASTICSEARCH_VIEWS should use this
	 * value<br>
	 * 
	 * @return name of the app used as index prefix
	 */
	String elasticsearchAppname();

}
