package es.prodevelop.pui9.model.dao.elasticsearch.utils;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import es.prodevelop.pui9.enums.GeometryType;
import es.prodevelop.pui9.model.dao.elasticsearch.PuiElasticSearchManager;
import es.prodevelop.pui9.model.dao.elasticsearch.config.IPuiElasticSearchSpringConfiguration;
import es.prodevelop.pui9.model.dao.elasticsearch.data.ESLanguagesEnum;
import es.prodevelop.pui9.model.dao.elasticsearch.data.MappingField;
import es.prodevelop.pui9.model.dao.elasticsearch.data.MappingMetadata;
import es.prodevelop.pui9.model.dao.elasticsearch.data.MappingTypesEnum;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

@Component
public class PuiElasticSearchIndexUtils {

	public static final String STRING_SEPARATOR = "_";

	private static final String NUMBER_OF_SHARDS = "1";
	private static final String NUMBER_OF_REPLICAS = "0";
	private static final Integer MAX_RESULT_WINDOW = 10000000;

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private PuiElasticSearchManager elasticSearchManager;

	@Autowired
	private DaoRegistry daoRegistry;

	/**
	 * Set the value in the {@link IPuiElasticSearchSpringConfiguration} class of
	 * your project
	 */
	@Autowired
	@Qualifier("elasticsearchAppname")
	private String elasticsearchAppname;

	private Map<Class<? extends IDto>, List<String>> cacheDtoIndices = new LinkedHashMap<>();
	private Map<String, PuiLanguage> cacheIndexLang = new LinkedHashMap<>();
	private Set<String> cacheIndicesMapping = new LinkedHashSet<>();
	private Gson gson = new GsonBuilder().create();

	@PostConstruct
	private void postConstruct() {
		new Thread(this::refreshIndicesCache, "ELASTICSEARCH_REFRESH_INDICES_CACHE").start();
	}

	/**
	 * Get the index prefix for the indices of ElasticSearch for this application
	 * 
	 * @return The prefix for the indices
	 */
	public String getIndexPrefix() {
		return elasticsearchAppname;
	}

	public void refresh() {
		cacheDtoIndices.clear();
		cacheIndexLang.clear();
	}

	public <T extends IDto> boolean createIndex(Class<T> dtoClass) {
		List<String> indices = getIndicesForDto(dtoClass);

		boolean created = true;
		for (String index : indices) {
			created &= createIndex(dtoClass, index);
		}

		return created;
	}

	public <T extends IDto> boolean createIndex(Class<T> dtoClass, String index) {
		PuiLanguage lang = new PuiLanguage(
				index.substring(index.lastIndexOf(PuiElasticSearchIndexUtils.STRING_SEPARATOR) + 1));
		if (ESLanguagesEnum.getByCode(lang.getIsocode()) == null) {
			lang = null;
		}

		MappingMetadata mapping = buildMapping(dtoClass, lang);
		String mappingJson = gson.toJson(mapping);

		IndexSettings indexSettings = IndexSettings.of(b -> b.numberOfShards(NUMBER_OF_SHARDS)
				.numberOfReplicas(NUMBER_OF_REPLICAS).maxResultWindow(MAX_RESULT_WINDOW));

		TypeMapping tm = TypeMapping.of(tm2 -> tm2.withJson(new StringReader(mappingJson)));

		CreateIndexRequest request = CreateIndexRequest
				.of(cir -> cir.settings(indexSettings).index(index).mappings(tm));
		CreateIndexResponse response;

		try {
			response = getClient().indices().create(request);
		} catch (ElasticsearchException | IOException e) {
			logger.debug("Could not create Index '" + index + "' because already exists");
			return false;
		}

		if (response.acknowledged()) {
			cacheIndicesMapping.add(index);
			return true;
		} else {
			logger.debug("Could not create Index '" + index + "'");
			return false;
		}
	}

	public <T extends IDto> boolean deleteIndex(Class<T> dtoClass) {
		List<String> indices = getIndicesForDto(dtoClass);

		boolean deleted = true;
		for (String index : indices) {
			deleted &= deleteIndex(index);
		}

		return deleted;
	}

	public boolean deleteIndex(String index) {
		DeleteIndexRequest request = DeleteIndexRequest.of(dir -> dir.index(index));
		DeleteIndexResponse response;

		try {
			response = getClient().indices().delete(request);
		} catch (ElasticsearchException | IOException e) {
			cacheIndicesMapping.remove(index);
			logger.debug("Could not remove Index '" + index + "' because doesn't exists");
			return false;
		}

		if (response.acknowledged()) {
			cacheIndicesMapping.remove(index);
			return true;
		} else {
			logger.debug("Could not remove Index '" + index + "'");
			return false;
		}
	}

	public <T extends IDto> boolean existIndex(Class<T> dtoClass) {
		List<String> indices = getIndicesForDto(dtoClass);

		boolean exists = true;
		for (String index : indices) {
			exists &= existsIndex(dtoClass, getLanguageFromIndex(index));
		}

		return exists;
	}

	public <T extends IDto> boolean existsIndex(Class<T> dtoClass, PuiLanguage language) {
		if (cacheIndicesMapping.isEmpty()) {
			refreshIndicesCache();
		}

		List<String> indices;
		if (language != null) {
			indices = Collections.singletonList(getIndexForLanguage(dtoClass, language));
		} else {
			indices = getIndicesForDto(dtoClass);
		}

		int existsNum = 0;
		for (String index : indices) {
			if (cacheIndicesMapping.contains(index)) {
				existsNum++;
			}
		}

		if (existsNum == indices.size()) {
			return true;
		} else {
			logger.debug("Indices " + indices + " don't exists");
			return false;
		}
	}

	public <T extends IDto> long countIndex(Class<T> dtoClass) {
		List<String> indices = getIndicesForDto(dtoClass);
		return countIndex(indices.get(0));
	}

	public long countIndex(String index) {
		CountRequest request = CountRequest.of(cr -> cr.index(index));
		try {
			return getClient().count(request).count();
		} catch (ElasticsearchException | IOException e) {
			return -1;
		}
	}

	/**
	 * Build the mapping for an Index represented by the IDto class and langugae
	 */
	public <T extends IDto> MappingMetadata buildMapping(Class<T> dtoClass, PuiLanguage language) {
		MappingMetadata mapping = new MappingMetadata();

		ESLanguagesEnum lang = null;
		if (language != null) {
			lang = ESLanguagesEnum.getByCode(language.getIsocode());
		} else {
			lang = ESLanguagesEnum.standard;
		}

		for (Field field : DtoRegistry.getAllJavaFields(dtoClass)) {
			if (Modifier.isTransient(field.getModifiers()) || Modifier.isVolatile(field.getModifiers())) {
				continue;
			}

			MappingTypesEnum type = null;
			if (DtoRegistry.getDateTimeFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._date;
			} else if (DtoRegistry.getNumericFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._long;
			} else if (DtoRegistry.getFloatingFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._double;
			} else if (DtoRegistry.getStringFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._text;
			} else if (DtoRegistry.getBooleanFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._boolean;
			}

			if (DtoRegistry.getGeomFields(dtoClass).contains(field.getName())) {
				GeometryType geomType = DtoRegistry.getGometryFieldType(dtoClass, field.getName());
				switch (geomType) {
				case NONE:
					throw new IllegalArgumentException(
							"Java field '" + field.getName() + "' of DTO '" + dtoClass.getSimpleName()
									+ "' should declare a Geometry Type. Renerate DTO should correct this");
				case POINT:
					type = MappingTypesEnum._geo_point;
					break;
				case LINESTRING:
				case POLYGON:
					type = MappingTypesEnum._geo_shape;
					break;
				}
			}

			boolean withKeyword = isTextTerm(dtoClass, field.getName());

			mapping.addMappingField(field.getName(), new MappingField(type, withKeyword, lang));
		}

		return mapping;
	}

	/**
	 * Return the list of indices for the given Dto.<br>
	 * <ul>
	 * <li>If the Dto has language support:</li>
	 * <ul>
	 * <li>If no language is specified: returns all the indices for each existing
	 * language</li>
	 * <li>If a language is specified: it returns only this index</li>
	 * </ul>
	 * <li>If Dto has no language support: it returns the only existing index for
	 * it</li>
	 * </ul>
	 * 
	 * @param dtoClass The {@link IDto} class that represents the index
	 * @return The list of indices of the given DTO class
	 */
	public List<String> getIndicesForDto(Class<? extends IDto> dtoClass) {
		if (dtoClass.isInterface()) {
			dtoClass = DtoFactory.getClassFromInterface(dtoClass);
		}

		if (!cacheDtoIndices.containsKey(dtoClass)) {
			String prefix = getIndexPrefix();
			String entity = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dtoClass));
			String indexName = ObjectUtils.isEmpty(prefix) ? entity : prefix + STRING_SEPARATOR + entity;
			List<String> indices = new ArrayList<>();

			if (DtoRegistry.getAllFields(dtoClass).contains(IDto.LANG_FIELD_NAME)) {
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					indices.add(indexName + STRING_SEPARATOR + it.next().getIsocode());
				}
			} else {
				indices.add(indexName);
			}

			cacheDtoIndices.put(dtoClass, indices);
		}

		return cacheDtoIndices.get(dtoClass);
	}

	/**
	 * Get the index for the given {@link IDto} class and language
	 * 
	 * @param dtoClass The {@link IDto} class that represents the index
	 * @param language The desired language
	 * @return The index for given DTO class and language
	 */
	public String getIndexForLanguage(Class<? extends IDto> dtoClass, PuiLanguage language) {
		List<String> indics = getIndicesForDto(dtoClass);
		if (indics.size() == 1) {
			return indics.get(0);
		} else {
			if (language == null) {
				return indics.get(0);
			} else {
				for (String index : indics) {
					if (index.endsWith(STRING_SEPARATOR + language.getIsocode())) {
						return index;
					}
				}
			}

			return null;
		}
	}

	/**
	 * Get the language for the given Index
	 * 
	 * @param index The index name
	 * @return The language of the index
	 */
	public PuiLanguage getLanguageFromIndex(String index) {
		if (!cacheIndexLang.containsKey(index)) {
			for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
				PuiLanguage next = it.next();
				if (index.endsWith(STRING_SEPARATOR + next.getIsocode())) {
					cacheIndexLang.put(index, next);
					break;
				}
			}
		}

		return cacheIndexLang.get(index);
	}

	/**
	 * Get all existing indices
	 * 
	 * @return
	 */
	public List<String> getAllIndices() {
		List<String> list = new ArrayList<>();
		if (cacheIndicesMapping.isEmpty()) {
			refreshIndicesCache();
		}
		list.addAll(new ArrayList<>(cacheIndicesMapping));
		return list;
	}

	/**
	 * Refresh the cache of indices
	 */
	private void refreshIndicesCache() {
		IndicesResponse response;
		try {
			response = getClient().cat().indices();
		} catch (ElasticsearchException | IOException e) {
			return;
		}

		if (ObjectUtils.isEmpty(response.valueBody())) {
			return;
		}

		cacheIndicesMapping.clear();

		// get only the indices of the application
		response.valueBody().stream().filter(ir -> ir.index().startsWith(getIndexPrefix()))
				.forEach(ir -> cacheIndicesMapping.add(ir.index()));
	}

	private <T extends IDto> boolean isTextTerm(Class<T> dtoClass, String fieldName) {
		Integer length = DtoRegistry.getFieldMaxLength(dtoClass, fieldName);
		return length == null || (length > 0 && length < (32 * 1024));
	}

	private ElasticsearchClient getClient() {
		return elasticSearchManager.getClient();
	}

}
