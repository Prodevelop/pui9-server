package es.prodevelop.pui9.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import es.prodevelop.pui9.filter.rules.EqualsRule;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.utils.IPuiObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

/**
 * This class represents a Filter to execute a customized search against the
 * database. The {@link SearchRequest} uses this kind of Filters so specify the
 * user search. See also {@link FilterBuilder} class to help building custom
 * filters
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Schema(description = "Filter group object")
public class FilterGroup implements IPuiObject {

	private static final long serialVersionUID = 1L;

	@Schema(description = "Operation", requiredMode = RequiredMode.REQUIRED)
	private FilterGroupOperation groupOp = FilterGroupOperation.and;

	@Schema(description = "Groups list", requiredMode = RequiredMode.NOT_REQUIRED)
	private List<FilterGroup> groups = new ArrayList<>();

	@Schema(description = "Rules list", requiredMode = RequiredMode.NOT_REQUIRED)
	private List<AbstractFilterRule> rules = new ArrayList<>();

	public static FilterGroup andGroup() {
		return new FilterGroup(FilterGroupOperation.and);
	}

	public static FilterGroup orGroup() {
		return new FilterGroup(FilterGroupOperation.or);
	}

	/**
	 * Create a Filter from the Dto PK provided with the values of the Primary Key
	 * of the registry
	 * 
	 * @param dto The table Dto registry
	 * @return A filter of the PK of the given Table Dto
	 */
	public static FilterGroup createFilterForDtoPk(ITableDto dto) {
		if (dto == null) {
			return null;
		}

		ITableDto dtoPk = dto.createPk();
		FilterGroup filter = andGroup();
		for (String fieldName : DtoRegistry.getAllFields(dtoPk.getClass())) {
			try {
				Field field = DtoRegistry.getJavaFieldFromFieldName(dtoPk.getClass(), fieldName);
				Object value = field.get(dtoPk);
				filter.getRules().add(EqualsRule.of(fieldName, value));
			} catch (Exception e) {
				// do nothing
			}
		}

		return !StringUtils.isEmpty(filter.toString()) ? filter : null;
	}

	/**
	 * Create a Filter from a json
	 * 
	 * @param json The filter as json
	 * @return The filter object
	 */
	public static FilterGroup fromJson(String json) {
		return GsonSingleton.getSingleton().getGson().fromJson(json, FilterGroup.class);
	}

	/**
	 * Creates a FiltersDto element with the given {@link FilterGroupOperation}
	 * operation
	 */
	private FilterGroup(FilterGroupOperation groupOp) {
		this.groupOp = groupOp;
	}

	/**
	 * Get the group operation. This operation is used for the whole group and rules
	 * 
	 * @return The Operation
	 */
	public FilterGroupOperation getGroupOp() {
		if (groupOp == null) {
			groupOp = FilterGroupOperation.and;
		}
		return groupOp;
	}

	public List<FilterGroup> getGroups() {
		if (groups == null) {
			groups = new ArrayList<>();
		}
		return groups;
	}

	public FilterGroup addGroup(FilterGroup group) {
		getGroups().add(group);
		return this;
	}

	public List<AbstractFilterRule> getRules() {
		if (rules == null) {
			rules = new ArrayList<>();
		}
		return rules;
	}

	/**
	 * Add the given Rule to this Group
	 * 
	 * @param rule The rule to be added
	 * @return This group modified
	 */
	public FilterGroup addRule(AbstractFilterRule rule) {
		getRules().add(rule);
		return this;
	}

	/**
	 * Convert the FilterGroup to JSON
	 * 
	 * @return The group as JSON
	 */
	public String toJson() {
		return GsonSingleton.getSingleton().getGson().toJson(this);
	}

	/**
	 * Return a copy of this FilterGroup
	 * 
	 * @return A copy of this FilterGroup
	 */
	public FilterGroup copy() {
		return GsonSingleton.getSingleton().getGson().fromJson(toJson(), FilterGroup.class);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (!ObjectUtils.isEmpty(getRules())) {
			for (Iterator<AbstractFilterRule> it = getRules().iterator(); it.hasNext();) {
				AbstractFilterRule next = it.next();
				sb.append("'" + next.toString() + "'");
				if (it.hasNext()) {
					sb.append(" " + getGroupOp() + " ");
				}
			}
		}

		if (!ObjectUtils.isEmpty(getGroups())) {
			if (!ObjectUtils.isEmpty(getRules())) {
				sb.append(" " + getGroupOp() + " ");
			}

			for (Iterator<FilterGroup> it = getGroups().iterator(); it.hasNext();) {

				FilterGroup next = it.next();
				sb.append("(" + next.toString() + ")");
				if (it.hasNext()) {
					sb.append(" " + getGroupOp() + " ");
				}
			}
		}

		return sb.toString();
	}

}
