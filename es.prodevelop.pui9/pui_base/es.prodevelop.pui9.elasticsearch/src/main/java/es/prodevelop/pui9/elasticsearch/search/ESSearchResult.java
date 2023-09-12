package es.prodevelop.pui9.elasticsearch.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This class is a representation of the search result against ElasticSearch. It
 * stores the time that the search took, the total number of registries and the
 * result items
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ESSearchResult<T extends IPuiObject> implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private Long total;
	private Long time;
	private List<ESSearchResultItem<T>> items;
	private Map<String, BigDecimal> sumData;

	/**
	 * Default constructor for Search
	 * 
	 * @param total The amount of registries that the search retrieved
	 * @param time  The time that the search took
	 */
	public ESSearchResult(Long total, Long time) {
		this.time = time;
		this.total = total;
		this.items = new ArrayList<>();
		this.sumData = new LinkedHashMap<>();
	}

	/**
	 * Get the total amount of records of the search
	 * 
	 * @return The total amount of records of the search
	 */
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * Get the time that the search took in milliseconds
	 * 
	 * @return The time that the search took in milliseconds
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * Add an item to the search result
	 * 
	 * @param item a Search item
	 */
	public void addItem(ESSearchResultItem<T> item) {
		items.add(item);
	}

	/**
	 * Get all the items of the search
	 * 
	 * @return The items of the search
	 */
	public List<ESSearchResultItem<T>> getItems() {
		return items;
	}

	/**
	 * Add a sum item
	 * 
	 * @param column The column to sum
	 * @param value  The value
	 */
	public void addSumData(String column, BigDecimal value) {
		sumData.put(column, value);
	}

	/**
	 * Get all the sum data items
	 * 
	 * @return The sum data items
	 */
	public Map<String, BigDecimal> getSumData() {
		return sumData;
	}

	/**
	 * Get the search result as list of {@link IViewDto}
	 * 
	 * @return The search result as list of {@link IViewDto}
	 */
	public List<T> getDtoList() {
		List<T> list = new ArrayList<>();

		for (ESSearchResultItem<T> item : items) {
			list.add(item.getDto());
		}

		return list;
	}

}
