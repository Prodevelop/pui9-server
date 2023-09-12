package es.prodevelop.pui9.elasticsearch.analysis;

import java.util.Map;

import org.jooq.Condition;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;

/**
 * Support class for the information of the SQL Join of the Views in the
 * database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class JoinTableDef {

	private Table<Record> table;
	private Name alias;
	private Condition joinCondition;
	private JoinType joinType;
	private Map<String, String> nameAliasMap;

	public JoinTableDef(Table<Record> table, Name alias, Condition joinCondition, JoinType joinType,
			Map<String, String> nameAliasMap) {
		this.table = table;
		this.alias = alias;
		this.joinCondition = joinCondition;
		this.joinType = joinType;
		this.nameAliasMap = nameAliasMap;
	}

	public Table<Record> getTable() {
		return table;
	}

	public Name getAlias() {
		return alias;
	}

	public Condition getJoinCondition() {
		return joinCondition;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	/**
	 * The map of name-alias of the columns of the view
	 * 
	 * @return A map of name-alias of the columns of the view
	 */
	public Map<String, String> getNameAliasMap() {
		return nameAliasMap;
	}

	@Override
	public String toString() {
		return joinType + " " + table + " " + (joinCondition != null ? joinCondition : "");
	}
}