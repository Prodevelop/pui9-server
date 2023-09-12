package es.prodevelop.pui9.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This object is used as response of the search request operation. It includes
 * the result of the operation, with the registries and paggination information:
 * <ul>
 * <li><b>currentPage</b>: the page returned (starting with 1)</li>
 * <li><b>currentRecords</b>: the number of records returned for the page</li>
 * <li><b>totalRecords</b>: the total amount of records that fit the given
 * search request</li>
 * <li><b>totalPages</b>: the total amount of pages that fit the given search
 * request</li>
 * <li><b>data</b>: the list of records</li>
 * </ul>
 * 
 * @param <T> The concrete type of the results
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class SearchResponse<TYPE> implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private Integer currentPage = SearchRequest.DEFAULT_PAGE;
	private Integer currentRecords = 0;
	private Long totalRecords = 0L;
	private Long totalPages = 1L;
	private List<TYPE> data = null;
	private Map<String, BigDecimal> sumData = null;

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getCurrentRecords() {
		return currentRecords;
	}

	public void setCurrentRecords(Integer currentRecords) {
		this.currentRecords = currentRecords;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}

	public List<TYPE> getData() {
		if (this.data == null) {
			this.data = new ArrayList<>();
		}
		return this.data;
	}

	public void addData(TYPE data) {
		if (this.data == null) {
			this.data = new ArrayList<>();
		}
		this.data.add(data);
	}

	public void setData(List<TYPE> data) {
		this.data = data;
	}

	public Map<String, BigDecimal> getSumData() {
		if (this.sumData == null) {
			this.sumData = new LinkedHashMap<>();
		}
		return this.sumData;
	}

	public void addSumData(String column, BigDecimal value) {
		if (this.sumData == null) {
			this.sumData = new LinkedHashMap<>();
		}
		this.sumData.put(column, value);
	}

	public void setSumData(Map<String, BigDecimal> sumData) {
		this.sumData = sumData;
	}

}
