package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BoolQueryBuilder extends AbstractQueryBuilder<BoolQueryBuilder> {
	
	public static final String NAME = "bool";

    public static final boolean ADJUST_PURE_NEGATIVE_DEFAULT = true;

    private static final String MUSTNOT = "mustNot";
    private static final String MUST_NOT = "must_not";
    private static final String FILTER = "filter";
    private static final String SHOULD = "should";
    private static final String MUST = "must";
    private static final ParseField DISABLE_COORD_FIELD = new ParseField("disable_coord")
            .withAllDeprecated("disable_coord has been removed");
    private static final ParseField MINIMUM_SHOULD_MATCH = new ParseField("minimum_should_match");
    private static final ParseField ADJUST_PURE_NEGATIVE = new ParseField("adjust_pure_negative");

    private final List<QueryBuilder> mustClauses = new ArrayList<>();

    private final List<QueryBuilder> mustNotClauses = new ArrayList<>();

    private final List<QueryBuilder> filterClauses = new ArrayList<>();

    private final List<QueryBuilder> shouldClauses = new ArrayList<>();

    private boolean adjustPureNegative = ADJUST_PURE_NEGATIVE_DEFAULT;

    private String minimumShouldMatch;

    /**
     * Build an empty bool query.
     */
    public BoolQueryBuilder() {
    }
    
    /**
     * Adds a query that <b>must</b> appear in the matching documents and will
     * contribute to scoring. No <tt>null</tt> value allowed.
     */
    public BoolQueryBuilder must(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        mustClauses.add(queryBuilder);
        return this;
    }
    
    /**
     * Gets the queries that <b>must</b> appear in the matching documents.
     */
    public List<QueryBuilder> must() {
        return this.mustClauses;
    }

    /**
     * Adds a query that <b>must</b> appear in the matching documents but will
     * not contribute to scoring. No <tt>null</tt> value allowed.
     */
    public BoolQueryBuilder filter(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        filterClauses.add(queryBuilder);
        return this;
    }

    /**
     * Gets the queries that <b>must</b> appear in the matching documents but don't contribute to scoring
     */
    public List<QueryBuilder> filter() {
        return this.filterClauses;
    }

    /**
     * Adds a query that <b>must not</b> appear in the matching documents.
     * No <tt>null</tt> value allowed.
     */
    public BoolQueryBuilder mustNot(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        mustNotClauses.add(queryBuilder);
        return this;
    }

    /**
     * Gets the queries that <b>must not</b> appear in the matching documents.
     */
    public List<QueryBuilder> mustNot() {
        return this.mustNotClauses;
    }

    /**
     * Adds a clause that <i>should</i> be matched by the returned documents. For a boolean query with no
     * <tt>MUST</tt> clauses one or more <code>SHOULD</code> clauses must match a document
     * for the BooleanQuery to match. No <tt>null</tt> value allowed.
     *
     * @see #minimumShouldMatch(int)
     */
    public BoolQueryBuilder should(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        shouldClauses.add(queryBuilder);
        return this;
    }

    /**
     * Gets the list of clauses that <b>should</b> be matched by the returned documents.
     *
     * @see #should(QueryBuilder)
     *  @see #minimumShouldMatch(int)
     */
    public List<QueryBuilder> should() {
        return this.shouldClauses;
    }

    /**
     * @return the string representation of the minimumShouldMatch settings for this query
     */
    public String minimumShouldMatch() {
        return this.minimumShouldMatch;
    }

    /**
     * Sets the minimum should match parameter using the special syntax (for example, supporting percentage).
     * @see BoolQueryBuilder#minimumShouldMatch(int)
     */
    public BoolQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    /**
     * Specifies a minimum number of the optional (should) boolean clauses which must be satisfied.
     * <p>
     * By default no optional clauses are necessary for a match
     * (unless there are no required clauses).  If this method is used,
     * then the specified number of clauses is required.
     * <p>
     * Use of this method is totally independent of specifying that
     * any specific clauses are required (or prohibited).  This number will
     * only be compared against the number of matching optional clauses.
     *
     * @param minimumShouldMatch the number of optional clauses that must match
     */
    public BoolQueryBuilder minimumShouldMatch(int minimumShouldMatch) {
        this.minimumShouldMatch = Integer.toString(minimumShouldMatch);
        return this;
    }

    /**
     * Returns <code>true</code> iff this query builder has at least one should, must, must not or filter clause.
     * Otherwise <code>false</code>.
     */
    public boolean hasClauses() {
        return !(mustClauses.isEmpty() && shouldClauses.isEmpty() && mustNotClauses.isEmpty() && filterClauses.isEmpty());
    }
    
    /**
     * If a boolean query contains only negative ("must not") clauses should the
     * BooleanQuery be enhanced with a {@link MatchAllDocsQuery} in order to act
     * as a pure exclude. The default is <code>true</code>.
     */
    public BoolQueryBuilder adjustPureNegative(boolean adjustPureNegative) {
        this.adjustPureNegative = adjustPureNegative;
        return this;
    }

    /**
     * @return the setting for the adjust_pure_negative setting in this query
     */
    public boolean adjustPureNegative() {
        return this.adjustPureNegative;
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected int doHashCode() {
		return Objects.hash(adjustPureNegative, minimumShouldMatch, mustClauses, shouldClauses, mustNotClauses,
				filterClauses);
	}

	@Override
	protected boolean doEquals(BoolQueryBuilder other) {
		return Objects.equals(adjustPureNegative, other.adjustPureNegative)
				&& Objects.equals(minimumShouldMatch, other.minimumShouldMatch)
				&& Objects.equals(mustClauses, other.mustClauses) && Objects.equals(shouldClauses, other.shouldClauses)
				&& Objects.equals(mustNotClauses, other.mustNotClauses)
				&& Objects.equals(filterClauses, other.filterClauses);
	}


	@Override
	protected Map<String, Object> getXContent(Map<String, Object> builder) throws IOException {
		 
		 Map<String,Object> boolMap = new LinkedHashMap<String,Object>();
	    doXArrayContent(MUST, mustClauses, boolMap);
        doXArrayContent(FILTER, filterClauses, boolMap);
        doXArrayContent(MUST_NOT, mustNotClauses, boolMap);
        doXArrayContent(SHOULD, shouldClauses, boolMap);
        boolMap.put(ADJUST_PURE_NEGATIVE.getPreferredName(), adjustPureNegative);
        if (minimumShouldMatch != null) {
        	boolMap.put(MINIMUM_SHOULD_MATCH.getPreferredName(), minimumShouldMatch);
        }
        printBoostAndQueryName(boolMap);
       
        builder.put(NAME, boolMap);
        return builder;    
	        
	        
	        
	}

	private void doXArrayContent(String field, List<QueryBuilder> clauses, Map<String, Object> builder)  throws IOException {
		if (clauses.isEmpty()) {
            return;
        }
		
		ArrayList list = new ArrayList<>();
//        builder.startArray(field);
        for (QueryBuilder clause : clauses) {
        	Map<String,Object> cMap = new HashMap<String,Object>();
            clause.toXContent(cMap);
            list.add(cMap);
        }
		builder.put(field, list);
	}

}
