package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Same as {@link MatchQueryBuilder} but supports multiple fields.
 */
public class MultiMatchQueryBuilder extends AbstractQueryBuilder<MultiMatchQueryBuilder> {

	public static final String NAME = "multi_match";
	
	public static final Type DEFAULT_TYPE = Type.BEST_FIELDS;
    public static final Operator DEFAULT_OPERATOR = Operator.OR;
    public static final int DEFAULT_PHRASE_SLOP = MatchQuery.DEFAULT_PHRASE_SLOP;
    public static final MatchQuery.ZeroTermsQuery DEFAULT_ZERO_TERMS_QUERY = MatchQuery.DEFAULT_ZERO_TERMS_QUERY;

    private static final ParseField SLOP_FIELD = new ParseField("slop");
    private static final ParseField ZERO_TERMS_QUERY_FIELD = new ParseField("zero_terms_query");
    private static final ParseField USE_DIS_MAX_FIELD = new ParseField("use_dis_max");
    private static final ParseField MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match");
    private static final ParseField OPERATOR_FIELD = new ParseField("operator");
    private static final ParseField ANALYZER_FIELD = new ParseField("analyzer");
    private static final ParseField TYPE_FIELD = new ParseField("type");
    private static final ParseField QUERY_FIELD = new ParseField("query");
    private static final ParseField FIELDS_FIELD = new ParseField("fields");
    private static final ParseField GENERATE_SYNONYMS_PHRASE_QUERY = new ParseField("auto_generate_synonyms_phrase_query");

    private final Object value;
    private final Map<String, Float> fieldsBoosts;
    private Type type = DEFAULT_TYPE;
    private Operator operator = DEFAULT_OPERATOR;
    private String analyzer;
    private int slop = DEFAULT_PHRASE_SLOP;

    private String minimumShouldMatch;
    private Boolean useDisMax;
    private MatchQuery.ZeroTermsQuery zeroTermsQuery = DEFAULT_ZERO_TERMS_QUERY;
    private boolean autoGenerateSynonymsPhraseQuery = true;

    public enum Type {

        /**
         * Uses the best matching boolean field as main score and uses
         * a tie-breaker to adjust the score based on remaining field matches
         */
        BEST_FIELDS(MatchQuery.Type.BOOLEAN, 0.0f, new ParseField("best_fields", "boolean")),

        /**
         * Uses the sum of the matching boolean fields to score the query
         */
        MOST_FIELDS(MatchQuery.Type.BOOLEAN, 1.0f, new ParseField("most_fields")),

        /**
         * Uses a blended DocumentFrequency to dynamically combine the queried
         * fields into a single field given the configured analysis is identical.
         * This type uses a tie-breaker to adjust the score based on remaining
         * matches per analyzed terms
         */
        CROSS_FIELDS(MatchQuery.Type.BOOLEAN, 0.0f, new ParseField("cross_fields")),

        /**
         * Uses the best matching phrase field as main score and uses
         * a tie-breaker to adjust the score based on remaining field matches
         */
        PHRASE(MatchQuery.Type.PHRASE, 0.0f, new ParseField("phrase")),

        /**
         * Uses the best matching phrase-prefix field as main score and uses
         * a tie-breaker to adjust the score based on remaining field matches
         */
        PHRASE_PREFIX(MatchQuery.Type.PHRASE_PREFIX, 0.0f, new ParseField("phrase_prefix"));

        private MatchQuery.Type matchQueryType;
        private final float tieBreaker;
        private final ParseField parseField;

        Type (MatchQuery.Type matchQueryType, float tieBreaker, ParseField parseField) {
            this.matchQueryType = matchQueryType;
            this.tieBreaker = tieBreaker;
            this.parseField = parseField;
        }

        public float tieBreaker() {
            return this.tieBreaker;
        }

        public MatchQuery.Type matchQueryType() {
            return matchQueryType;
        }

        public ParseField parseField() {
            return parseField;
        }

        public static Type parse(String value) {
            Type[] values = Type.values();
            Type type = null;
            for (Type t : values) {
                if (t.parseField().match(value)) {
                    type = t;
                    break;
                }
            }
            if (type == null) {
                throw new RuntimeException("failed to parse " + NAME + " query type " + value +". unknown type.");
            }
            return type;
        }

    }

    
    /**
     * Returns the type (for testing)
     */
    public Type getType() {
        return type;
    }

    /**
     * Constructs a new text query.
     */
    public MultiMatchQueryBuilder(Object value, String... fields) {
        if (value == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires query value");
        }
        if (fields == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires fields at initialization time");
        }
        this.value = value;
        this.fieldsBoosts = new TreeMap<>();
        for (String field : fields) {
            field(field);
        }
    }
    
    public Object value() {
        return value;
    }

    /**
     * Adds a field to run the multi match against.
     */
    public MultiMatchQueryBuilder field(String field) {
        if (Strings.isEmpty(field)) {
            throw new IllegalArgumentException("supplied field is null or empty.");
        }
        this.fieldsBoosts.put(field, AbstractQueryBuilder.DEFAULT_BOOST);
        return this;
    }
    
    /**
     * Adds a field to run the multi match against with a specific boost.
     */
    public MultiMatchQueryBuilder field(String field, float boost) {
        if (Strings.isEmpty(field)) {
            throw new IllegalArgumentException("supplied field is null or empty.");
        }
        this.fieldsBoosts.put(field, boost);
        return this;
    }

    /**
     * Add several fields to run the query against with a specific boost.
     */
    public MultiMatchQueryBuilder fields(Map<String, Float> fields) {
        this.fieldsBoosts.putAll(fields);
        return this;
    }
    
    public Map<String, Float> fields() {
        return fieldsBoosts;
    }

    /**
     * Sets the type of the text query.
     */
    public MultiMatchQueryBuilder type(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires type to be non-null");
        }
        this.type = type;
        return this;
    }
    
    /**
     * Sets the type of the text query.
     */
    public MultiMatchQueryBuilder type(Object type) {
        if (type == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires type to be non-null");
        }
        this.type = Type.parse(type.toString().toLowerCase());
        return this;
    }

    public Type type() {
        return type;
    }
    
    /**
     * Sets the operator to use when using a boolean query. Defaults to <tt>OR</tt>.
     */
    public MultiMatchQueryBuilder operator(Operator operator) {
        if (operator == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires operator to be non-null");
        }
        this.operator = operator;
        return this;
    }

    public Operator operator() {
        return operator;
    }

    /**
     * Explicitly set the analyzer to use. Defaults to use explicit mapping config for the field, or, if not
     * set, the default search analyzer.
     */
    public MultiMatchQueryBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public String analyzer() {
        return analyzer;
    }

    /**
     * Set the phrase slop if evaluated to a phrase query type.
     */
    public MultiMatchQueryBuilder slop(int slop) {
        if (slop < 0) {
            throw new IllegalArgumentException("No negative slop allowed.");
        }
        this.slop = slop;
        return this;
    }

    public int slop() {
        return slop;
    }

    public String minimumShouldMatch() {
        return minimumShouldMatch;
    }

    
    /**
     * @deprecated use a tieBreaker of 1.0f to disable "dis-max"
     * query or select the appropriate {@link Type}
     */
    @Deprecated
    public MultiMatchQueryBuilder useDisMax(Boolean useDisMax) {
        this.useDisMax = useDisMax;
        return this;
    }

    public Boolean useDisMax() {
        return useDisMax;
    }
    
    public MultiMatchQueryBuilder zeroTermsQuery(MatchQuery.ZeroTermsQuery zeroTermsQuery) {
        if (zeroTermsQuery == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires zero terms query to be non-null");
        }
        this.zeroTermsQuery = zeroTermsQuery;
        return this;
    }

    public MatchQuery.ZeroTermsQuery zeroTermsQuery() {
        return zeroTermsQuery;
    }

    public MultiMatchQueryBuilder autoGenerateSynonymsPhraseQuery(boolean enable) {
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
    public String getName() {
        return NAME;
    }
    
	@Override
	protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put(QUERY_FIELD.getPreferredName(), value);
		List<String> fieldsList = new ArrayList<String>();
		for (Map.Entry<String, Float> fieldEntry : this.fieldsBoosts.entrySet()) {
			fieldsList.add(fieldEntry.getKey() + "^" + fieldEntry.getValue());
        }
		queryMap.put(FIELDS_FIELD.getPreferredName(), fieldsList);
		queryMap.put(TYPE_FIELD.getPreferredName(), type.toString().toLowerCase(Locale.ENGLISH));
		queryMap.put(OPERATOR_FIELD.getPreferredName(), operator.toString());  
		 if (analyzer != null) {
			 queryMap.put(ANALYZER_FIELD.getPreferredName(), analyzer);
        }
		queryMap.put(SLOP_FIELD.getPreferredName(), slop);
        if (minimumShouldMatch != null) {
        		queryMap.put(MINIMUM_SHOULD_MATCH_FIELD.getPreferredName(), minimumShouldMatch);
        }
        if (useDisMax != null) {
        		queryMap.put(USE_DIS_MAX_FIELD.getPreferredName(), useDisMax);
        }
        queryMap.put(ZERO_TERMS_QUERY_FIELD.getPreferredName(), zeroTermsQuery.toString());
        queryMap.put(GENERATE_SYNONYMS_PHRASE_QUERY.getPreferredName(), autoGenerateSynonymsPhraseQuery);
	    printBoostAndQueryName(queryMap);
	    builder.put(NAME, queryMap);
		return builder;
	}
    
    @Override
    protected int doHashCode() {
        return Objects.hash(value, fieldsBoosts, type, operator, analyzer, slop,
                 minimumShouldMatch, useDisMax,
                 zeroTermsQuery, autoGenerateSynonymsPhraseQuery);
    }
    
    @Override
    protected boolean doEquals(MultiMatchQueryBuilder other) {
        return Objects.equals(value, other.value) &&
                Objects.equals(fieldsBoosts, other.fieldsBoosts) &&
                Objects.equals(type, other.type) &&
                Objects.equals(operator, other.operator) &&
                Objects.equals(analyzer, other.analyzer) &&
                Objects.equals(slop, other.slop) &&
                Objects.equals(minimumShouldMatch, other.minimumShouldMatch) &&
                Objects.equals(useDisMax, other.useDisMax) &&
                Objects.equals(zeroTermsQuery, other.zeroTermsQuery) &&
                Objects.equals(autoGenerateSynonymsPhraseQuery, other.autoGenerateSynonymsPhraseQuery);
    }
    
}
