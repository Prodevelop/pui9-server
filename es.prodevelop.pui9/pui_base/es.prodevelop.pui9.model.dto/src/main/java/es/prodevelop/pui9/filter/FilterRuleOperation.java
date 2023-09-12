package es.prodevelop.pui9.filter;

import es.prodevelop.pui9.filter.rules.BeginWithRule;
import es.prodevelop.pui9.filter.rules.BetweenRule;
import es.prodevelop.pui9.filter.rules.BoundingBoxRule;
import es.prodevelop.pui9.filter.rules.ContainsRule;
import es.prodevelop.pui9.filter.rules.EndsWithRule;
import es.prodevelop.pui9.filter.rules.EqualsRule;
import es.prodevelop.pui9.filter.rules.EqualsThanTodayRule;
import es.prodevelop.pui9.filter.rules.GreaterEqualsThanRule;
import es.prodevelop.pui9.filter.rules.GreaterEqualsThanTodayRule;
import es.prodevelop.pui9.filter.rules.GreaterThanRule;
import es.prodevelop.pui9.filter.rules.GreaterThanTodayRule;
import es.prodevelop.pui9.filter.rules.InRule;
import es.prodevelop.pui9.filter.rules.IntersectsByPointRule;
import es.prodevelop.pui9.filter.rules.IsNotNullRule;
import es.prodevelop.pui9.filter.rules.IsNullRule;
import es.prodevelop.pui9.filter.rules.LowerEqualsThanRule;
import es.prodevelop.pui9.filter.rules.LowerEqualsThanTodayRule;
import es.prodevelop.pui9.filter.rules.LowerThanRule;
import es.prodevelop.pui9.filter.rules.LowerThanTodayRule;
import es.prodevelop.pui9.filter.rules.NotBeginWithRule;
import es.prodevelop.pui9.filter.rules.NotBetweenRule;
import es.prodevelop.pui9.filter.rules.NotContainsRule;
import es.prodevelop.pui9.filter.rules.NotEndsWithRule;
import es.prodevelop.pui9.filter.rules.NotEqualsRule;
import es.prodevelop.pui9.filter.rules.NotEqualsThanTodayRule;
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
	eq(EqualsRule.class),

	/**
	 * equals to today
	 */
	eqt(EqualsThanTodayRule.class),

	/**
	 * not equals
	 */
	ne(NotEqualsRule.class),

	/**
	 * not equals to today
	 */
	net(NotEqualsThanTodayRule.class),

	/**
	 * is null
	 */
	nu(IsNullRule.class),

	/**
	 * not null
	 */
	nn(IsNotNullRule.class),

	/**
	 * begins with
	 */
	bw(BeginWithRule.class),

	/**
	 * not begins with
	 */
	bn(NotBeginWithRule.class),

	/**
	 * contains
	 */
	cn(ContainsRule.class),

	/**
	 * not contains
	 */
	nc(NotContainsRule.class),

	/**
	 * ends with
	 */
	ew(EndsWithRule.class),

	/**
	 * not ends with
	 */
	en(NotEndsWithRule.class),

	/**
	 * greater than
	 */
	gt(GreaterThanRule.class),

	/**
	 * greater than today
	 */
	gtt(GreaterThanTodayRule.class),

	/**
	 * greater or equals than
	 */
	ge(GreaterEqualsThanRule.class),

	/**
	 * greater or equals than today
	 */
	get(GreaterEqualsThanTodayRule.class),

	/**
	 * less than
	 */
	lt(LowerThanRule.class),

	/**
	 * less than today
	 */
	ltt(LowerThanTodayRule.class),

	/**
	 * less or equals than
	 */
	le(LowerEqualsThanRule.class),

	/**
	 * less or equals than today
	 */
	let(LowerEqualsThanTodayRule.class),

	/**
	 * in
	 */
	in(InRule.class),

	/**
	 * not in
	 */
	ni(NotInRule.class),

	/**
	 * between
	 */
	bt(BetweenRule.class),

	/**
	 * not between
	 */
	nbt(NotBetweenRule.class),

	/**
	 * Bounding Box (for GEO)
	 */
	boundingBox(BoundingBoxRule.class),

	/**
	 * Bounding Box (for GEO)
	 */
	intersects(IntersectsByPointRule.class);

	public final Class<? extends AbstractFilterRule> clazz;

	private FilterRuleOperation(Class<? extends AbstractFilterRule> clazz) {
		this.clazz = clazz;
	}

}