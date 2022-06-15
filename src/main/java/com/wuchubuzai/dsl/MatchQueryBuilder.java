package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Match query is a query that analyzes the text and constructs a query as the
 * result of the analysis.
 */
public class MatchQueryBuilder extends AbstractQueryBuilder<MatchQueryBuilder>{
	
	public static final ParseField ZERO_TERMS_QUERY_FIELD = new ParseField("zero_terms_query");
	public static final ParseField MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match");
	public static final ParseField OPERATOR_FIELD = new ParseField("operator");
	public static final ParseField ANALYZER_FIELD = new ParseField("analyzer");
	public static final ParseField QUERY_FIELD = new ParseField("query");
	public static final ParseField GENERATE_SYNONYMS_PHRASE_QUERY = new ParseField("auto_generate_synonyms_phrase_query");

	 /** The name for the match query */
    public static final String NAME = "match";

    /** The default mode terms are combined in a match query */
    public static final Operator DEFAULT_OPERATOR = Operator.OR;
	
	private final String fieldName;

    private final Object value;

    private Operator operator;

    private String analyzer;

    private String minimumShouldMatch;

    private MatchQuery.ZeroTermsQuery zeroTermsQuery;

    private boolean autoGenerateSynonymsPhraseQuery = true;

    
    /**
     * Constructs a new match query.
     */
    public MatchQueryBuilder(String fieldName, Object value) {
        if (fieldName == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires fieldName");
        }
        if (value == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires query value");
        }
        this.fieldName = fieldName;
        this.value = value;
    }
    
    /** Returns the field name used in this query. */
    public String fieldName() {
        return this.fieldName;
    }

    /** Returns the value used in this query. */
    public Object value() {
        return this.value;
    }

    /** Sets the operator to use when using a boolean query. Defaults to <tt>OR</tt>. */
    public MatchQueryBuilder operator(Operator operator) {
        if (operator == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires operator to be non-null");
        }
        this.operator = operator;
        return this;
    }
    
    /** Returns the operator to use in a boolean query.*/
    public Operator operator() {
        return this.operator;
    }
    
    /**
     * Explicitly set the analyzer to use. Defaults to use explicit mapping config for the field, or, if not
     * set, the default search analyzer.
     */
    public MatchQueryBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    /** Get the analyzer to use, if previously set, otherwise <tt>null</tt> */
    public String analyzer() {
        return this.analyzer;
    }
    
    /** Sets optional minimumShouldMatch value to apply to the query */
    public MatchQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    /** Gets the minimumShouldMatch value */
    public String minimumShouldMatch() {
        return this.minimumShouldMatch;
    }
    
    /**
     * Sets query to use in case no query terms are available, e.g. after analysis removed them.
     * Defaults to {@link MatchQuery.ZeroTermsQuery#NONE}, but can be set to
     * {@link MatchQuery.ZeroTermsQuery#ALL} instead.
     */
    public MatchQueryBuilder zeroTermsQuery(MatchQuery.ZeroTermsQuery zeroTermsQuery) {
        if (zeroTermsQuery == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires zeroTermsQuery to be non-null");
        }
        this.zeroTermsQuery = zeroTermsQuery;
        return this;
    }

    /**
     * Returns the setting for handling zero terms queries.
     */
    public MatchQuery.ZeroTermsQuery zeroTermsQuery() {
        return this.zeroTermsQuery;
    }

    public MatchQueryBuilder autoGenerateSynonymsPhraseQuery(boolean enable) {
        this.autoGenerateSynonymsPhraseQuery = enable;
        return this;
    }
    
    /**
     * Whether phrase queries should be automatically generated for multi terms synonyms.
     * Defaults to <tt>true</tt>.
     */
    public boolean autoGenerateSynonymsPhraseQuery() {
        return autoGenerateSynonymsPhraseQuery;
    }
    
    @Override
    protected boolean doEquals(MatchQueryBuilder other) {
        return Objects.equals(fieldName, other.fieldName) &&
               Objects.equals(value, other.value) &&
               Objects.equals(operator, other.operator) &&
               Objects.equals(analyzer, other.analyzer) &&
               Objects.equals(minimumShouldMatch, other.minimumShouldMatch) &&
               Objects.equals(zeroTermsQuery, other.zeroTermsQuery) &&
               Objects.equals(autoGenerateSynonymsPhraseQuery, other.autoGenerateSynonymsPhraseQuery);
    }

    @Override
    protected int doHashCode() {
        return Objects.hash(fieldName, value, operator, analyzer, minimumShouldMatch,
                 zeroTermsQuery, autoGenerateSynonymsPhraseQuery);
    }
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put(QUERY_FIELD.getPreferredName(), value);
		if(operator != null){
			queryMap.put(OPERATOR_FIELD.getPreferredName(), operator.toString());
		}
		if (analyzer != null) {
			queryMap.put(ANALYZER_FIELD.getPreferredName(), analyzer);
	    }
		if (minimumShouldMatch != null) {
			queryMap.put(MINIMUM_SHOULD_MATCH_FIELD.getPreferredName(), minimumShouldMatch);
		}
		if(zeroTermsQuery != null){
			queryMap.put(ZERO_TERMS_QUERY_FIELD.getPreferredName(), zeroTermsQuery.toString());
		}
		queryMap.put(GENERATE_SYNONYMS_PHRASE_QUERY.getPreferredName(), autoGenerateSynonymsPhraseQuery);
		printBoostAndQueryName(queryMap);
		Map<String,Object> fieldMap = new HashMap<String,Object>();
		fieldMap.put(fieldName, queryMap);
		
		builder.put(NAME, fieldMap);
		return builder;
	}
    
	
    

}
