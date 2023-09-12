package es.prodevelop.pui9.elasticsearch.analysis;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiElasticsearchViewsDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.db.helpers.IDatabaseHelper;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.elasticsearch.config.IPuiElasticSearchSpringConfiguration;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;

/**
 * This component process all the indexable views for the application set in the
 * table PUI_ELASTICSEARCH_VIEWS. The indexable views are application dependent,
 * and the identification of each application are set by the
 * "elasticsearchAppname" bean in the
 * {@link IPuiElasticSearchSpringConfiguration}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticsearchViewsAnalysis {

	private final Logger logger = LogManager.getLogger(this.getClass());
	private final String[] translationSuffixes = { "_tr", "_tra", "_tran", "_trans", "_translation" };

	@Autowired
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired
	private IPuiElasticsearchViewsDao elasticsearchViewsDao;

	@Autowired
	private IPuiModelService modelService;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private List<DataSource> dataSources;

	@Autowired
	private IDatabaseHelper databaseHelper;

	/**
	 * Set the value in the {@link IPuiElasticSearchSpringConfiguration} class of
	 * your project
	 */
	@Autowired
	@Qualifier("elasticsearchAppname")
	private String elasticsearchAppname;

	private List<IPuiElasticsearchViews> indexableViews;
	private List<String> indexableModels;

	/**
	 * Table -> (View -> List<LinkedList<JoinTableDef>>)
	 */
	private Map<String, Map<String, List<LinkedList<JoinTableDef>>>> mapInfo = new LinkedHashMap<>();

	@EventListener
	public void onApplicationEvent(ContextStartedEvent event) {
		refresh();
	}

	private Connection getConnection(int dataSourceIndex) {
		try {
			return DataSourceUtils.getConnection(dataSources.get(dataSourceIndex));
		} catch (Exception e) {
			return null;
		}
	}

	private void releaseConnection(Connection connection, int dataSourceIndex) {
		try {
			DataSourceUtils.releaseConnection(connection, dataSources.get(dataSourceIndex));
		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * Reset the cache of the indexable views
	 */
	public void refresh() {
		getIndexableViewsFromDatabase();
		setIndexableViewsToElastic();
		parseViews();
	}

	private void getIndexableViewsFromDatabase() {
		try {
			indexableViews = elasticsearchViewsDao.findByAppname(elasticsearchAppname);
		} catch (PuiDaoFindException e) {
			indexableViews = Collections.emptyList();
		}

		List<IPuiModel> models;
		try {
			models = modelService.getTableDao().findAll();
		} catch (PuiDaoFindException e) {
			models = Collections.emptyList();
		}

		indexableModels = new ArrayList<>();
		indexableModels.addAll(models.stream()
				.filter(m -> indexableViews.stream().map(IPuiElasticsearchViews::getViewname)
						.collect(Collectors.toList()).contains(m.getEntity().toLowerCase()))
				.map(IPuiModel::getModel).collect(Collectors.toList()));
	}

	@SuppressWarnings("unchecked")
	private void setIndexableViewsToElastic() {
		indexableViews.forEach(iv -> {
			Class<? extends IDto> dtoClass = daoRegistry.getDtoFromEntityName(iv.getParsedViewName(), false, false);
			if (dtoClass == null) {
				return;
			}

			if (IViewDto.class.isAssignableFrom(dtoClass)) {
				elasticSearchEnablement.addIndexableView((Class<? extends IViewDto>) dtoClass);
			}
		});
	}

	private void parseViews() {
		logger.debug("*** View analysis start");
		Map<String, String> mapViewCode = new LinkedHashMap<>();
		long start = System.currentTimeMillis();

		indexableViews.forEach(iv -> {
			org.jooq.Select<Record> select = databaseHelper
					.getViewsSql(Collections.singletonList(iv.getParsedViewName()));
			int i = 0;
			Connection connection;
			while ((connection = getConnection(i)) != null) {
				try (ResultSet rs = connection.prepareStatement(select.getSQL()).executeQuery()) {
					while (rs.next()) {
						String viewName = rs.getString(1).toLowerCase();
						String code = rs.getString(2);
						if (!ObjectUtils.isEmpty(viewName) && !ObjectUtils.isEmpty(code)) {
							mapViewCode.put(viewName.toLowerCase(), code);
						}
					}
					releaseConnection(connection, i);
					break;
				} catch (SQLException e) {
					// do nothing
				}
			}
		});

		mapViewCode.forEach((viewName, code) -> {
			try {
				CCJSqlParserManager pm = new CCJSqlParserManager();
				Statement stm = pm.parse(new StringReader(code));

				if (stm instanceof CreateView) {
					parseCreateView(viewName, (CreateView) stm);
				} else if (stm instanceof Select) {
					parseSelectBody(viewName, ((Select) stm).getSelectBody());
				}
			} catch (JSQLParserException e) {
				logger.debug(viewName + ": " + e.getCause().getMessage(), e);
			}
		});

		logger.debug("*** View analysis finish");
		long end = System.currentTimeMillis();
		logger.debug("*** View analysis time: " + (end - start));
	}

	/**
	 * Parse an statement of type Create for the given view
	 * 
	 * @param viewName   The analyzed view
	 * @param createView The Create code
	 */
	private void parseCreateView(String viewName, CreateView createView) {
		if (createView.getSelect().getSelectBody() instanceof PlainSelect) {
			parseSelectBody(viewName, createView.getSelect().getSelectBody());
		} else if (createView.getSelect().getSelectBody() instanceof SetOperationList) {
			SetOperationList set = (SetOperationList) createView.getSelect().getSelectBody();
			for (SelectBody selectBody : set.getSelects()) {
				parseSelectBody(viewName, selectBody);
			}
		}
	}

	/**
	 * Parse an statement of type Select for the given view
	 * 
	 * @param viewName   The analyzed view
	 * @param selectBody The Select code
	 */
	private void parseSelectBody(String viewName, SelectBody selectBody) {
		if (selectBody instanceof PlainSelect) {
			parsePlainSelect(viewName, (PlainSelect) selectBody);
		} else if (selectBody instanceof SetOperationList) {
			SetOperationList set = (SetOperationList) selectBody;
			for (SelectBody select : set.getSelects()) {
				parseSelectBody(viewName, select);
			}
		}
	}

	/**
	 * Parse an statement of type Plain Select for the given view. At the end, the
	 * goal is to retrieve the list of tables and views that participate in the
	 * given view in any way: from or join
	 * 
	 * @param viewName    The analyzed view
	 * @param plainSelect The Plain Select code
	 */
	private void parsePlainSelect(String viewName, PlainSelect plainSelect) {
		if (!(plainSelect.getFromItem() instanceof Table)) {
			return;
		}

		Map<String, String> mapAliasToTable = new LinkedHashMap<>();
		Map<String, Condition> mapTableSql = new LinkedHashMap<>();
		Map<String, String> columnNameAliasMap = new LinkedHashMap<>();

		Table fromTable = (Table) plainSelect.getFromItem();
		String fromTableName = fromTable.getName().toLowerCase();
		String fromTableAlias = fromTable.getAlias() != null ? fromTable.getAlias().getName() : null;
		if (fromTableAlias != null) {
			mapAliasToTable.put(fromTableAlias.toLowerCase(), fromTableName);
		}

		mapTableSql.put(fromTableName, null);
		addTable(fromTableName);
		addView(fromTableName, viewName);
		addTableOrderList(fromTableName, viewName);

		for (SelectItem si : plainSelect.getSelectItems()) {
			if (!(si instanceof SelectExpressionItem)) {
				continue;
			}

			SelectExpressionItem sei = (SelectExpressionItem) si;
			Column c = lookForColumn(sei.getExpression());
			if (c == null) {
				continue;
			}

			if (c.getTable() == null || c.getTable().getName() == null
					|| c.getTable().getName().equalsIgnoreCase(fromTableName)
					|| c.getTable().getName().equals(fromTableAlias)) {
				String name = c.getColumnName().toLowerCase();
				String alias = name;
				if (sei.getAlias() != null) {
					alias = sei.getAlias().getName().toLowerCase();
				}
				if (!columnNameAliasMap.containsKey(name)) {
					columnNameAliasMap.put(name, alias);
				}
			}
		}

		if (plainSelect.getJoins() != null) {
			for (Join join : plainSelect.getJoins()) {
				if (!(join.getRightItem() instanceof Table)) {
					continue;
				}

				Table joinTable = (Table) join.getRightItem();
				String joinTableName = joinTable.getName().toLowerCase();
				if (isTranslationTable(joinTableName)) {
					continue;
				}

				String joinTableAlias = joinTable.getAlias() != null ? joinTable.getAlias().getName() : null;
				if (joinTableAlias != null) {
					mapAliasToTable.put(joinTableAlias.toLowerCase(), joinTableName);
				}
				addTable(joinTableName);
				addView(joinTableName, viewName);
				addTableOrderList(joinTableName, viewName);

				Expression onExpression = join.getOnExpressions().stream().findFirst().orElse(null);
				if (onExpression == null) {
					return;
				}

				Condition condition = databaseHelper.getDSLContext().parser().parseCondition(onExpression.toString());
				mapTableSql.put(joinTableName, condition);
				addTableOrder(joinTableName, viewName, new JoinTableDef(DSL.table(joinTableName),
						DSL.unquotedName(joinTableAlias), condition, JoinType.JOIN, columnNameAliasMap));

				EqualsTo eto = null;
				if (onExpression instanceof EqualsTo) {
					eto = (EqualsTo) onExpression;
				} else if (onExpression instanceof BinaryExpression) {
					BinaryExpression be = (BinaryExpression) onExpression;
					if (be.getLeftExpression() instanceof EqualsTo) {
						eto = (EqualsTo) be.getLeftExpression();
					} else if (be.getRightExpression() instanceof EqualsTo) {
						eto = (EqualsTo) be.getRightExpression();
					}
				}

				if (eto == null) {
					continue;
				}

				Column leftCol = lookForColumn(eto.getLeftExpression());
				Column rightCol = lookForColumn(eto.getRightExpression());

				if (leftCol == null || rightCol == null) {
					continue;
				}

				String leftColAlias = leftCol.getTable().getName();
				String rightColAlias = rightCol.getTable().getName();

				if (leftColAlias.equalsIgnoreCase(fromTableAlias != null ? fromTableAlias : fromTableName)
						|| rightColAlias.equalsIgnoreCase(fromTableAlias != null ? fromTableAlias : fromTableName)) {
					String relatedTableName = null;
					if (mapAliasToTable.containsKey(
							fromTableAlias != null ? fromTableAlias.toLowerCase() : fromTableName.toLowerCase())) {
						relatedTableName = mapAliasToTable.get(
								fromTableAlias != null ? fromTableAlias.toLowerCase() : fromTableName.toLowerCase());
					} else {
						relatedTableName = fromTableName;
					}

					condition = mapTableSql.get(relatedTableName);
					addTableOrder(joinTableName, viewName,
							new JoinTableDef(DSL.table(relatedTableName), DSL.name(fromTableAlias), condition,
									condition != null ? JoinType.JOIN : JoinType.FROM, columnNameAliasMap));
				} else {
					String relatedTableName = null;
					if (leftColAlias.equalsIgnoreCase(joinTableAlias != null ? joinTableAlias : joinTableName)) {
						if (mapAliasToTable.containsKey(rightColAlias.toLowerCase())) {
							relatedTableName = mapAliasToTable.get(rightColAlias.toLowerCase());
						} else {
							relatedTableName = rightColAlias.toLowerCase();
						}
					} else if (rightColAlias
							.equalsIgnoreCase(joinTableAlias != null ? joinTableAlias : joinTableName)) {
						if (mapAliasToTable.containsKey(leftColAlias.toLowerCase())) {
							relatedTableName = mapAliasToTable.get(leftColAlias.toLowerCase());
						} else {
							relatedTableName = leftColAlias.toLowerCase();
						}
					}

					for (JoinTableDef relateds : getTableViewOrder(relatedTableName, viewName)) {
						addTableOrder(joinTableName, viewName, relateds);
					}
				}
			}
		}

		addTableOrder(fromTableName, viewName, new JoinTableDef(DSL.table(fromTableName), DSL.name(fromTableAlias),
				null, JoinType.FROM, columnNameAliasMap));
	}

	private Column lookForColumn(Expression expression) {
		if (expression instanceof Column) {
			return (Column) expression;
		} else if (expression instanceof CastExpression) {
			CastExpression ce = (CastExpression) expression;
			return lookForColumn(ce.getLeftExpression());
		} else if (expression instanceof ComparisonOperator) {
			ComparisonOperator co = (ComparisonOperator) expression;
			return lookForColumn(co.getLeftExpression());
		} else if (expression instanceof Function) {
			// not supported
			Function f = (Function) expression;
			for (Expression e : f.getParameters().getExpressions()) {
				Column c = lookForColumn(e);
				if (c != null) {
					return c;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * Check if the given table is a translation table or not
	 * 
	 * @param table The table to check
	 * @return true is it's a translation table; false if not
	 */
	private boolean isTranslationTable(String table) {
		for (String suffix : translationSuffixes) {
			if (table.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add the given table to the list of analyzed tables
	 * 
	 * @param table The table to add
	 */
	private void addTable(String table) {
		mapInfo.computeIfAbsent(table, t -> new LinkedHashMap<>());
	}

	/**
	 * Add the given view as participating view for the given table
	 * 
	 * @param table The table where the view is participating
	 * @param view  The view where the table participates
	 */
	private void addView(String table, String view) {
		if (!mapInfo.get(table).containsKey(view)) {
			mapInfo.get(table).put(view, new ArrayList<>());
		}
	}

	/**
	 * Initialize a new table order list
	 * 
	 * @param table
	 * @param view
	 */
	private void addTableOrderList(String table, String view) {
		mapInfo.get(table).get(view).add(new LinkedList<>());
	}

	/**
	 * Add a new tableOrder definition for given table and view
	 * 
	 * @param table      The table where the view is participating
	 * @param view       The view where the table participates
	 * @param tableOrder The tableOrder definition
	 */
	private void addTableOrder(String table, String view, JoinTableDef tableOrder) {
		List<LinkedList<JoinTableDef>> list = mapInfo.get(table).get(view);
		if (!list.get(list.size() - 1).contains(tableOrder)) {
			list.get(list.size() - 1).add(tableOrder);
		}
	}

	/**
	 * Get the ordered list of joins of the given table for the given view
	 * 
	 * @param table The table
	 * @param view  The view
	 * @return The ordered list of joins
	 */
	private LinkedList<JoinTableDef> getTableViewOrder(String table, String view) {
		List<LinkedList<JoinTableDef>> list = mapInfo.get(table).get(view);
		return list.get(list.size() - 1);
	}

	/**
	 * Get the list of indexable views in Elastic Serach
	 * 
	 * @return The list of indexable views in Elastic Search
	 */
	public List<IPuiElasticsearchViews> getIndexableViews() {
		return indexableViews;
	}

	/**
	 * Get the list of indexable models in Elastic Serach
	 * 
	 * @return The list of indexable models in Elastic Search
	 */
	public List<String> getIndexableModels() {
		return indexableModels;
	}

	/**
	 * Get the list of ordered joins that the given table uses to participate in the
	 * given view. The order returned is in inverse mode, where the last item is the
	 * FROM join
	 * 
	 * @param table The table to retrieve the used joins to participate in the given
	 *              view
	 * @param view  The view where to check the table participation
	 * @return A list of ordered lists with the joins used in the view to make the
	 *         table participate on it. The order returned is in inverse mode, where
	 *         the last item is the FROM join
	 */
	public List<LinkedList<JoinTableDef>> getTableOrder(String table, String view) {
		return mapInfo.get(table) != null ? mapInfo.get(table).get(view) : new LinkedList<>();
	}

	/**
	 * Get all the views where the given table participates in any way (with from or
	 * join)
	 * 
	 * @param table The table to retrieve its views participation
	 * @return The list of views where the given table participates
	 */
	public Set<String> getViewsUsingTable(String table) {
		return mapInfo.get(table) != null ? mapInfo.get(table).keySet() : Collections.emptySet();
	}

	/**
	 * Get the list of related tables for the given views
	 * 
	 * @param view The view get the related tables
	 * @return The list of tables that participate in the given view
	 */
	public Set<String> getTablesParticipatingInView(String view) {
		Set<String> tables = new HashSet<>();

		for (Entry<String, Map<String, List<LinkedList<JoinTableDef>>>> entry : mapInfo.entrySet()) {
			if (entry.getValue().keySet().contains(view)) {
				tables.add(entry.getKey());
			}
		}

		return tables;
	}

}
