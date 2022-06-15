package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import com.wuchubuzai.dsl.MultiMatchQueryBuilder.Type;

public class QueryStringQueryBuilder extends AbstractQueryBuilder<QueryStringQueryBuilder> {

	public static final String NAME = "query_string";
    public static final int DEFAULT_MAX_DETERMINED_STATES = 10000;
    public static final boolean DEFAULT_ENABLE_POSITION_INCREMENTS = true;
    public static final boolean DEFAULT_ESCAPE = false;
    public static final int DEFAULT_FUZZY_PREFIX_LENGTH = 0;
    public static final int DEFAULT_FUZZY_MAX_EXPANSIONS = 50;
    public static final int DEFAULT_PHRASE_SLOP = 0;
    public static final Operator DEFAULT_OPERATOR;
    public static final Type DEFAULT_TYPE;
    public static final boolean DEFAULT_FUZZY_TRANSPOSITIONS = true;
    private static final ParseField QUERY_FIELD;
    private static final ParseField FIELDS_FIELD;
    private static final ParseField DEFAULT_FIELD_FIELD;
    private static final ParseField DEFAULT_OPERATOR_FIELD;
    private static final ParseField ANALYZER_FIELD;
    private static final ParseField QUOTE_ANALYZER_FIELD;
    private static final ParseField ALLOW_LEADING_WILDCARD_FIELD;
    private static final ParseField AUTO_GENERATE_PHRASE_QUERIES_FIELD;
    private static final ParseField MAX_DETERMINIZED_STATES_FIELD;
    private static final ParseField LOWERCASE_EXPANDED_TERMS_FIELD;
    private static final ParseField ENABLE_POSITION_INCREMENTS_FIELD;
    private static final ParseField ESCAPE_FIELD;
    private static final ParseField USE_DIS_MAX_FIELD;
    private static final ParseField FUZZY_PREFIX_LENGTH_FIELD;
    private static final ParseField FUZZY_MAX_EXPANSIONS_FIELD;
    private static final ParseField FUZZY_REWRITE_FIELD;
    private static final ParseField PHRASE_SLOP_FIELD;
    private static final ParseField TIE_BREAKER_FIELD;
    private static final ParseField ANALYZE_WILDCARD_FIELD;
    private static final ParseField REWRITE_FIELD;
    private static final ParseField MINIMUM_SHOULD_MATCH_FIELD;
    private static final ParseField QUOTE_FIELD_SUFFIX_FIELD;
    private static final ParseField LENIENT_FIELD;
    private static final ParseField LOCALE_FIELD;
    private static final ParseField SPLIT_ON_WHITESPACE;
    private static final ParseField ALL_FIELDS_FIELD;
    private static final ParseField TYPE_FIELD;
    private static final ParseField GENERATE_SYNONYMS_PHRASE_QUERY;
    private static final ParseField FUZZY_TRANSPOSITIONS_FIELD;
    private final String queryString;
    private String defaultField;
    private final Map<String, Float> fieldsAndWeights = new TreeMap();
    private Operator defaultOperator;
    private String analyzer;
    private String quoteAnalyzer;
    private String quoteFieldSuffix;
    private Boolean allowLeadingWildcard;
    private Boolean analyzeWildcard;
    private boolean enablePositionIncrements;
    private int fuzzyPrefixLength;
    private int fuzzyMaxExpansions;
    private String rewrite;
    private String fuzzyRewrite;
    private boolean escape;
    private int phraseSlop;
    private Type type;
    private Float tieBreaker;
    private String minimumShouldMatch;
    private Boolean lenient;
    private int maxDeterminizedStates;
    private boolean autoGenerateSynonymsPhraseQuery;
    private boolean fuzzyTranspositions;
	
    public QueryStringQueryBuilder(String queryString) {
        this.defaultOperator = DEFAULT_OPERATOR;
        this.enablePositionIncrements = true;
        this.fuzzyPrefixLength = 0;
        this.fuzzyMaxExpansions = 50;
        this.escape = false;
        this.phraseSlop = 0;
        this.type = DEFAULT_TYPE;
        this.maxDeterminizedStates = 10000;
        this.autoGenerateSynonymsPhraseQuery = true;
        this.fuzzyTranspositions = true;
        if (queryString == null) {
            throw new IllegalArgumentException("query text missing");
        } else {
            this.queryString = queryString;
        }
    }
    
    public String queryString() {
        return this.queryString;
    }

    public QueryStringQueryBuilder defaultField(String defaultField) {
        this.defaultField = defaultField;
        return this;
    }

    public String defaultField() {
        return this.defaultField;
    }

    /** @deprecated */
    @Deprecated
    public QueryStringQueryBuilder useAllFields(Boolean useAllFields) {
        if (useAllFields != null && useAllFields) {
            this.defaultField = "*";
        }

        return this;
    }

    /** @deprecated */
    @Deprecated
    public Boolean useAllFields() {
        return this.defaultField == null ? null : Regex.isMatchAllPattern(this.defaultField);
    }
    
    public QueryStringQueryBuilder field(String field) {
        this.fieldsAndWeights.put(field, 1.0F);
        return this;
    }

    public QueryStringQueryBuilder field(String field, float boost) {
        this.checkNegativeBoost(boost);
        this.fieldsAndWeights.put(field, boost);
        return this;
    }

    public QueryStringQueryBuilder fields(Map<String, Float> fields) {
        Iterator var2 = fields.values().iterator();
        while(var2.hasNext()) {
            float fieldBoost = (Float)var2.next();
            this.checkNegativeBoost(fieldBoost);
        }
        this.fieldsAndWeights.putAll(fields);
        return this;
    }
    
    public Map<String, Float> fields() {
        return this.fieldsAndWeights;
    }

    public QueryStringQueryBuilder type(Type type) {
        this.type = type;
        return this;
    }

    /** @deprecated */
    @Deprecated
    public QueryStringQueryBuilder useDisMax(boolean useDisMax) {
        return this;
    }

    /** @deprecated */
    @Deprecated
    public boolean useDisMax() {
        return true;
    }
    
    public QueryStringQueryBuilder tieBreaker(float tieBreaker) {
        this.tieBreaker = tieBreaker;
        return this;
    }

    public Float tieBreaker() {
        return this.tieBreaker;
    }

    public QueryStringQueryBuilder defaultOperator(Operator defaultOperator) {
        this.defaultOperator = defaultOperator == null ? DEFAULT_OPERATOR : defaultOperator;
        return this;
    }

    public Operator defaultOperator() {
        return this.defaultOperator;
    }

    public QueryStringQueryBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public String analyzer() {
        return this.analyzer;
    }

    public String quoteAnalyzer() {
        return this.quoteAnalyzer;
    }

    public QueryStringQueryBuilder quoteAnalyzer(String quoteAnalyzer) {
        this.quoteAnalyzer = quoteAnalyzer;
        return this;
    }
    
    /** @deprecated */
    @Deprecated
    public QueryStringQueryBuilder autoGeneratePhraseQueries(boolean autoGeneratePhraseQueries) {
        return this;
    }

    /** @deprecated */
    @Deprecated
    public boolean autoGeneratePhraseQueries() {
        return false;
    }

    public QueryStringQueryBuilder maxDeterminizedStates(int maxDeterminizedStates) {
        this.maxDeterminizedStates = maxDeterminizedStates;
        return this;
    }

    public int maxDeterminizedStates() {
        return this.maxDeterminizedStates;
    }

    public QueryStringQueryBuilder allowLeadingWildcard(Boolean allowLeadingWildcard) {
        this.allowLeadingWildcard = allowLeadingWildcard;
        return this;
    }

    public Boolean allowLeadingWildcard() {
        return this.allowLeadingWildcard;
    }

    public QueryStringQueryBuilder enablePositionIncrements(boolean enablePositionIncrements) {
        this.enablePositionIncrements = enablePositionIncrements;
        return this;
    }

    public boolean enablePositionIncrements() {
        return this.enablePositionIncrements;
    }

    public QueryStringQueryBuilder fuzzyPrefixLength(int fuzzyPrefixLength) {
        this.fuzzyPrefixLength = fuzzyPrefixLength;
        return this;
    }

    public int fuzzyPrefixLength() {
        return this.fuzzyPrefixLength;
    }

    public QueryStringQueryBuilder fuzzyMaxExpansions(int fuzzyMaxExpansions) {
        this.fuzzyMaxExpansions = fuzzyMaxExpansions;
        return this;
    }
    
    public int fuzzyMaxExpansions() {
        return this.fuzzyMaxExpansions;
    }

    public QueryStringQueryBuilder fuzzyRewrite(String fuzzyRewrite) {
        this.fuzzyRewrite = fuzzyRewrite;
        return this;
    }

    public String fuzzyRewrite() {
        return this.fuzzyRewrite;
    }

    public QueryStringQueryBuilder phraseSlop(int phraseSlop) {
        this.phraseSlop = phraseSlop;
        return this;
    }

    public int phraseSlop() {
        return this.phraseSlop;
    }

    public QueryStringQueryBuilder rewrite(String rewrite) {
        this.rewrite = rewrite;
        return this;
    }

    public QueryStringQueryBuilder analyzeWildcard(Boolean analyzeWildcard) {
        this.analyzeWildcard = analyzeWildcard;
        return this;
    }

    public Boolean analyzeWildcard() {
        return this.analyzeWildcard;
    }

    public String rewrite() {
        return this.rewrite;
    }

    public QueryStringQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public String minimumShouldMatch() {
        return this.minimumShouldMatch;
    }

    public QueryStringQueryBuilder quoteFieldSuffix(String quoteFieldSuffix) {
        this.quoteFieldSuffix = quoteFieldSuffix;
        return this;
    }

    public String quoteFieldSuffix() {
        return this.quoteFieldSuffix;
    }

    public QueryStringQueryBuilder lenient(Boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    public Boolean lenient() {
        return this.lenient;
    }

    public QueryStringQueryBuilder escape(boolean escape) {
        this.escape = escape;
        return this;
    }

    public boolean escape() {
        return this.escape;
    }

    /** @deprecated */
    @Deprecated
    public QueryStringQueryBuilder splitOnWhitespace(boolean value) {
        return this;
    }

    /** @deprecated */
    @Deprecated
    public boolean splitOnWhitespace() {
        return false;
    }

    public QueryStringQueryBuilder autoGenerateSynonymsPhraseQuery(boolean value) {
        this.autoGenerateSynonymsPhraseQuery = value;
        return this;
    }

    public boolean autoGenerateSynonymsPhraseQuery() {
        return this.autoGenerateSynonymsPhraseQuery;
    }

    public boolean fuzzyTranspositions() {
        return this.fuzzyTranspositions;
    }

    public QueryStringQueryBuilder fuzzyTranspositions(boolean fuzzyTranspositions) {
        this.fuzzyTranspositions = fuzzyTranspositions;
        return this;
    }
    
    @Override
    protected boolean doEquals(QueryStringQueryBuilder other) {
        return Objects.equals(this.queryString, other.queryString) && Objects.equals(this.defaultField, other.defaultField) && Objects.equals(this.fieldsAndWeights, other.fieldsAndWeights) && Objects.equals(this.defaultOperator, other.defaultOperator) && Objects.equals(this.analyzer, other.analyzer) && Objects.equals(this.quoteAnalyzer, other.quoteAnalyzer) && Objects.equals(this.quoteFieldSuffix, other.quoteFieldSuffix) && Objects.equals(this.allowLeadingWildcard, other.allowLeadingWildcard) && Objects.equals(this.enablePositionIncrements, other.enablePositionIncrements) && Objects.equals(this.analyzeWildcard, other.analyzeWildcard) && Objects.equals(this.fuzzyPrefixLength, other.fuzzyPrefixLength) && Objects.equals(this.fuzzyMaxExpansions, other.fuzzyMaxExpansions) && Objects.equals(this.fuzzyRewrite, other.fuzzyRewrite) && Objects.equals(this.phraseSlop, other.phraseSlop) && Objects.equals(this.type, other.type) && Objects.equals(this.tieBreaker, other.tieBreaker) && Objects.equals(this.rewrite, other.rewrite) && Objects.equals(this.minimumShouldMatch, other.minimumShouldMatch) && Objects.equals(this.lenient, other.lenient) && Objects.equals(this.escape, other.escape) && Objects.equals(this.maxDeterminizedStates, other.maxDeterminizedStates) && Objects.equals(this.autoGenerateSynonymsPhraseQuery, other.autoGenerateSynonymsPhraseQuery) && Objects.equals(this.fuzzyTranspositions, other.fuzzyTranspositions);
    }

    @Override
    protected int doHashCode() {
        return Objects.hash(new Object[]{this.queryString, this.defaultField, this.fieldsAndWeights, this.defaultOperator, this.analyzer, this.quoteAnalyzer, this.quoteFieldSuffix, this.allowLeadingWildcard, this.analyzeWildcard, this.enablePositionIncrements,  this.fuzzyPrefixLength, this.fuzzyMaxExpansions, this.fuzzyRewrite, this.phraseSlop, this.type, this.tieBreaker, this.rewrite, this.minimumShouldMatch, this.lenient, this.escape, this.maxDeterminizedStates, this.autoGenerateSynonymsPhraseQuery, this.fuzzyTranspositions});
    }
    
    @Override
    public String getName() {
        return NAME;
    }
    
    static {
        DEFAULT_OPERATOR = Operator.OR;
        DEFAULT_TYPE = Type.BEST_FIELDS;
        QUERY_FIELD = new ParseField("query", new String[0]);
        FIELDS_FIELD = new ParseField("fields", new String[0]);
        DEFAULT_FIELD_FIELD = new ParseField("default_field", new String[0]);
        DEFAULT_OPERATOR_FIELD = new ParseField("default_operator", new String[0]);
        ANALYZER_FIELD = new ParseField("analyzer", new String[0]);
        QUOTE_ANALYZER_FIELD = new ParseField("quote_analyzer", new String[0]);
        ALLOW_LEADING_WILDCARD_FIELD = new ParseField("allow_leading_wildcard", new String[0]);
        AUTO_GENERATE_PHRASE_QUERIES_FIELD = (new ParseField("auto_generate_phrase_queries", new String[0])).withAllDeprecated("This setting is ignored, use [type=phrase] instead to make phrase queries out of all text that is within query operators, or use explicitly quoted strings if you need finer-grained control");
        MAX_DETERMINIZED_STATES_FIELD = new ParseField("max_determinized_states", new String[0]);
        LOWERCASE_EXPANDED_TERMS_FIELD = (new ParseField("lowercase_expanded_terms", new String[0])).withAllDeprecated("Decision is now made by the analyzer");
        ENABLE_POSITION_INCREMENTS_FIELD = new ParseField("enable_position_increments", new String[0]);
        ESCAPE_FIELD = new ParseField("escape", new String[0]);
        USE_DIS_MAX_FIELD = (new ParseField("use_dis_max", new String[0])).withAllDeprecated("Set [tie_breaker] to 1 instead");
        FUZZY_PREFIX_LENGTH_FIELD = new ParseField("fuzzy_prefix_length", new String[0]);
        FUZZY_MAX_EXPANSIONS_FIELD = new ParseField("fuzzy_max_expansions", new String[0]);
        FUZZY_REWRITE_FIELD = new ParseField("fuzzy_rewrite", new String[0]);
        PHRASE_SLOP_FIELD = new ParseField("phrase_slop", new String[0]);
        TIE_BREAKER_FIELD = new ParseField("tie_breaker", new String[0]);
        ANALYZE_WILDCARD_FIELD = new ParseField("analyze_wildcard", new String[0]);
        REWRITE_FIELD = new ParseField("rewrite", new String[0]);
        MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match", new String[0]);
        QUOTE_FIELD_SUFFIX_FIELD = new ParseField("quote_field_suffix", new String[0]);
        LENIENT_FIELD = new ParseField("lenient", new String[0]);
        LOCALE_FIELD = (new ParseField("locale", new String[0])).withAllDeprecated("Decision is now made by the analyzer");
        SPLIT_ON_WHITESPACE = (new ParseField("split_on_whitespace", new String[0])).withAllDeprecated("This setting is ignored, the parser always splits on operator");
        ALL_FIELDS_FIELD = (new ParseField("all_fields", new String[0])).withAllDeprecated("Set [default_field] to `*` instead");
        TYPE_FIELD = new ParseField("type", new String[0]);
        GENERATE_SYNONYMS_PHRASE_QUERY = new ParseField("auto_generate_synonyms_phrase_query", new String[0]);
        FUZZY_TRANSPOSITIONS_FIELD = new ParseField("fuzzy_transpositions", new String[0]);
    }
    
    @Override
	protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put(QUERY_FIELD.getPreferredName(), this.queryString);
		if (this.defaultField != null) {
			queryMap.put(DEFAULT_FIELD_FIELD.getPreferredName(), this.defaultField);
        }
		List<String> fields = new ArrayList<String>();
        Iterator var3 = this.fieldsAndWeights.entrySet().iterator();
        while(var3.hasNext()) {
            Entry<String, Float> fieldEntry = (Entry)var3.next();
            fields.add((String)fieldEntry.getKey() + "^" + fieldEntry.getValue());
        }
		queryMap.put(FIELDS_FIELD.getPreferredName(), fields);
		if (this.type != null) {
			queryMap.put(TYPE_FIELD.getPreferredName(), this.type.toString().toLowerCase(Locale.ENGLISH));
		}
	    if (this.tieBreaker != null) {
	    	queryMap.put(TIE_BREAKER_FIELD.getPreferredName(), this.tieBreaker);
	    }		
	    queryMap.put(DEFAULT_OPERATOR_FIELD.getPreferredName(), this.defaultOperator.name().toLowerCase(Locale.ROOT));
        if (this.analyzer != null) {
        	queryMap.put(ANALYZER_FIELD.getPreferredName(), this.analyzer);
        }
        if (this.quoteAnalyzer != null) {
        	queryMap.put(QUOTE_ANALYZER_FIELD.getPreferredName(), this.quoteAnalyzer);
        }
        queryMap.put(MAX_DETERMINIZED_STATES_FIELD.getPreferredName(), this.maxDeterminizedStates);
        if (this.allowLeadingWildcard != null) {
        	queryMap.put(ALLOW_LEADING_WILDCARD_FIELD.getPreferredName(), this.allowLeadingWildcard);
        }
        queryMap.put(ENABLE_POSITION_INCREMENTS_FIELD.getPreferredName(), this.enablePositionIncrements);
        queryMap.put(FUZZY_PREFIX_LENGTH_FIELD.getPreferredName(), this.fuzzyPrefixLength);
        queryMap.put(FUZZY_MAX_EXPANSIONS_FIELD.getPreferredName(), this.fuzzyMaxExpansions);
        if (this.fuzzyRewrite != null) {
        	queryMap.put(FUZZY_REWRITE_FIELD.getPreferredName(), this.fuzzyRewrite);
        }
        queryMap.put(PHRASE_SLOP_FIELD.getPreferredName(), this.phraseSlop);
        if (this.analyzeWildcard != null) {
        	queryMap.put(ANALYZE_WILDCARD_FIELD.getPreferredName(), this.analyzeWildcard);
        }
        if (this.rewrite != null) {
        	queryMap.put(REWRITE_FIELD.getPreferredName(), this.rewrite);
        }
        if (this.minimumShouldMatch != null) {
        	queryMap.put(MINIMUM_SHOULD_MATCH_FIELD.getPreferredName(), this.minimumShouldMatch);
        }
        if (this.quoteFieldSuffix != null) {
        	queryMap.put(QUOTE_FIELD_SUFFIX_FIELD.getPreferredName(), this.quoteFieldSuffix);
        }
        if (this.lenient != null) {
        	queryMap.put(LENIENT_FIELD.getPreferredName(), this.lenient);
        }
        queryMap.put(ESCAPE_FIELD.getPreferredName(), this.escape);
        queryMap.put(GENERATE_SYNONYMS_PHRASE_QUERY.getPreferredName(), this.autoGenerateSynonymsPhraseQuery);
        queryMap.put(FUZZY_TRANSPOSITIONS_FIELD.getPreferredName(), this.fuzzyTranspositions);
		
		this.printBoostAndQueryName(builder);
		builder.put(NAME, queryMap);
		return builder;
    }
    
}
