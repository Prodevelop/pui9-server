package es.prodevelop.pui9.elasticsearch.services;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCountException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.model.dao.elasticsearch.utils.PuiElasticSearchIndexUtils;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * Implementation for the API to manage Indices for ElasticSaerch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchIndexService {

	private static final String NULL_VIEW = "NullView";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private IPuiElasticSearchEnablement puiElasticSearchEnablement;

	@Autowired
	private PuiElasticSearchIndexUtils indexUtils;

	/**
	 * Get the prefix for the indices
	 * 
	 * @return The index prefix
	 */
	public String getIndexPrefix() {
		return indexUtils.getIndexPrefix();
	}

	/**
	 * Create the indices related with the view that is represented by the given
	 * View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @throws PuiElasticSearchCreateIndexException If any error occurs while
	 *                                              creating the Index
	 */
	public void createIndex(Class<? extends IViewDto> dtoClass) throws PuiElasticSearchCreateIndexException {
		createIndex(dtoClass, null);
	}

	/**
	 * Create the index related with the view that is represented by the given View
	 * DTO Class, for the given language
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @param language The language that represents the index to be created. May be
	 *                 null, in that case an index for each existing language will
	 *                 be created
	 * @throws PuiElasticSearchCreateIndexException If any error occurs while
	 *                                              creating the Index
	 */
	public void createIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchCreateIndexException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchCreateIndexException(NULL_VIEW);
		}

		if (!puiElasticSearchEnablement.isViewIndexable(dtoClass)) {
			return;
		}

		List<String> indices;
		if (language != null) {
			indices = Collections.singletonList(indexUtils.getIndexForLanguage(dtoClass, language));
		} else {
			indices = indexUtils.getIndicesForDto(dtoClass);
		}

		for (String index : indices) {
			boolean created = indexUtils.createIndex(dtoClass, index);
			if (!created) {
				throw new PuiElasticSearchCreateIndexException(index);
			}
		}
	}

	/**
	 * Delete the given index
	 * 
	 * @param index The Index to be deleted
	 * @throws PuiElasticSearchDeleteIndexException If any error occurs while
	 *                                              deleting the Index
	 */
	public void deleteIndex(String index) throws PuiElasticSearchDeleteIndexException {
		boolean deleted = indexUtils.deleteIndex(index);
		if (!deleted) {
			throw new PuiElasticSearchDeleteIndexException(index);
		}
	}

	/**
	 * Delete the indices related with the view that is represented by the given
	 * View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @throws PuiElasticSearchDeleteIndexException If any error occurs while
	 *                                              deleting the Index
	 */
	public void deleteIndex(Class<? extends IViewDto> dtoClass) throws PuiElasticSearchDeleteIndexException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			return;
		}

		boolean deleted = indexUtils.deleteIndex(dtoClass);
		if (!deleted) {
			throw new PuiElasticSearchDeleteIndexException("Indices of " + dtoClass.getSimpleName());
		}
	}

	/**
	 * Check if the index that belongs to the given View DTO Class exists or not
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @return true if the index exists, false if not
	 */
	public boolean existsIndex(Class<? extends IViewDto> dtoClass) {
		return existsIndex(dtoClass, null);
	}

	/**
	 * Check if the index that belongs to the given View DTO Class exists or not,
	 * for the given language
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @param language The language that represents the index to be checked. May be
	 *                 null, in that case an index for each existing language will
	 *                 be checked
	 * @return true if the index exists, false if not
	 */
	public boolean existsIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language) {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			return false;
		}

		return indexUtils.existsIndex(dtoClass, language);
	}

	/**
	 * Get the list of existing indices names
	 * 
	 * @return The list of existing indices names
	 */
	public List<String> getAllIndices() {
		return indexUtils.getAllIndices();
	}

	/**
	 * Get the number of indexed documents for the given View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @return The number of indexed documents
	 */
	public long countIndex(Class<? extends IViewDto> dtoClass) throws PuiElasticSearchCountException {
		long count = indexUtils.countIndex(dtoClass);
		if (count == -1) {
			throw new PuiElasticSearchCountException("Index for class " + dtoClass.getSimpleName());
		}
		return count;
	}

	/**
	 * Get the number of indexed documents for the given View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @param language The language that represents the index to be checked. May be
	 *                 null, in that case an index for each existing language will
	 *                 be checked
	 * @return The number of indexed documents
	 * @throws PuiElasticSearchCountException If multiple indices (languages) are
	 *                                        checked and the value is distinct for
	 *                                        each one
	 */
	public long countIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchCountException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchCountException(NULL_VIEW);
		}

		List<String> indices;
		if (language != null) {
			indices = Collections.singletonList(indexUtils.getIndexForLanguage(dtoClass, language));
		} else {
			indices = indexUtils.getIndicesForDto(dtoClass);
		}

		long total = -1;
		for (String index : indices) {
			long count = indexUtils.countIndex(index);
			if (total == -1) {
				total = count;
			} else if (total != count) {
				logger.debug("Index '" + index + "' with distinct documents in each language");
				throw new PuiElasticSearchCountException(index);
			}
		}

		return total;
	}

	/**
	 * Get the number of registries for the given indexex
	 * 
	 * @param indices The list of the indices to retrieve the count
	 * @return The number of registries for each index
	 */
	public Map<String, Long> countIndex(List<String> indices) {
		if (indices.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, Long> map = new LinkedHashMap<>();
		indices.forEach(index -> {
			long count = indexUtils.countIndex(index);
			map.put(index, count);
		});

		return map;
	}

}