package es.prodevelop.pui9.filter;

import java.util.List;

import es.prodevelop.pui9.filter.rules.BeginWithRule;
import es.prodevelop.pui9.filter.rules.BetweenRule;
import es.prodevelop.pui9.filter.rules.BoundingBoxRule;
import es.prodevelop.pui9.filter.rules.ContainsRule;
import es.prodevelop.pui9.filter.rules.EndsWithRule;
import es.prodevelop.pui9.filter.rules.EqualsRule;
import es.prodevelop.pui9.filter.rules.GreaterEqualsThanRule;
import es.prodevelop.pui9.filter.rules.GreaterThanRule;
import es.prodevelop.pui9.filter.rules.InRule;
import es.prodevelop.pui9.filter.rules.IntersectsByPointRule;
import es.prodevelop.pui9.filter.rules.IsNotNullRule;
import es.prodevelop.pui9.filter.rules.IsNullRule;
import es.prodevelop.pui9.filter.rules.LowerEqualsThanRule;
import es.prodevelop.pui9.filter.rules.LowerThanRule;
import es.prodevelop.pui9.filter.rules.NotBeginWithRule;
import es.prodevelop.pui9.filter.rules.NotBetweenRule;
import es.prodevelop.pui9.filter.rules.NotContainsRule;
import es.prodevelop.pui9.filter.rules.NotEndsWithRule;
import es.prodevelop.pui9.filter.rules.NotEqualsRule;
import es.prodevelop.pui9.filter.rules.NotInRule;

/**
 * Filter rule operation enumeration. Values are case sensitive.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public enum FilterRuleOperation {

	/**
	 * equals
	 */
	eq(EqualsRule.class, Object.class),

	/**
	 * equals to today
	 */
	eqt(EqualsRule.class, TodayRuleData.class),

	/**
	 * not equals
	 */
	ne(NotEqualsRule.class, Object.class),

	/**
	 * not equals to today
	 */
	net(NotEqualsRule.class, TodayRuleData.class),

	/**
	 * is null
	 */
	nu(IsNullRule.class, null),

	/**
	 * not null
	 */
	nn(IsNotNullRule.class, null),

	/**
	 * begins with
	 */
	bw(BeginWithRule.class, String.class),

	/**
	 * not begins with
	 */
	bn(NotBeginWithRule.class, String.class),

	/**
	 * contains
	 */
	cn(ContainsRule.class, String.class),

	/**
	 * not contains
	 */
	nc(NotContainsRule.class, String.class),

	/**
	 * ends with
	 */
	ew(EndsWithRule.class, String.class),

	/**
	 * not ends with
	 */
	en(NotEndsWithRule.class, String.class),

	/**
	 * greater than
	 */
	gt(GreaterThanRule.class, Object.class),

	/**
	 * greater than today
	 */
	gtt(GreaterThanRule.class, TodayRuleData.class),

	/**
	 * greater or equals than
	 */
	ge(GreaterEqualsThanRule.class, Object.class),

	/**
	 * greater or equals than today
	 */
	get(GreaterEqualsThanRule.class, TodayRuleData.class),

	/**
	 * less than
	 */
	lt(LowerThanRule.class, Object.class),

	/**
	 * less than today
	 */
	ltt(LowerThanRule.class, TodayRuleData.class),

	/**
	 * less or equals than
	 */
	le(LowerEqualsThanRule.class, Object.class),

	/**
	 * less or equals than today
	 */
	let(LowerEqualsThanRule.class, TodayRuleData.class),

	/**
	 * in
	 */
	in(InRule.class, List.class),

	/**
	 * not in
	 */
	ni(NotInRule.class, List.class),

	/**
	 * between
	 */
	bt(BetweenRule.class, List.class),

	/**
	 * not between
	 */
	nbt(NotBetweenRule.class, List.class),

	/**
	 * Bounding Box (for GEO)
	 */
	boundingBox(BoundingBoxRule.class, null),

	/**
	 * Bounding Box (for GEO)
	 */
	intersects(IntersectsByPointRule.class, null);

	public final Class<? extends AbstractFilterRule> clazz;
	public final Class<?> dataClass;

	private FilterRuleOperation(Class<? extends AbstractFilterRule> clazz, Class<?> dataClass) {
		this.clazz = clazz;
		this.dataClass = dataClass;
	}

}