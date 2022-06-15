package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Match query is a query that analyzes the text and constructs a phrase prefix
 * query as the result of the analysis.
 */
public class MatchPhrasePrefixQueryBuilder  extends AbstractQueryBuilder<MatchPhrasePrefixQueryBuilder> 	{

	public static final String NAME = "match_phrase_prefix";

    private final String fieldName;

    private final Object value;

    private String analyzer;

    private int slop = MatchQuery.DEFAULT_PHRASE_SLOP;

	
    public MatchPhrasePrefixQueryBuilder(String fieldName, Object value) {
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

    /**
     * Explicitly set the analyzer to use. Defaults to use explicit mapping
     * config for the field, or, if not set, the default search analyzer.
     */
    public MatchPhrasePrefixQueryBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    /** Get the analyzer to use, if previously set, otherwise <tt>null</tt> */
    public String analyzer() {
        return this.analyzer;
    }

    /** Sets a slop factor for phrase queries */
    public MatchPhrasePrefixQueryBuilder slop(int slop) {
        if (slop < 0) {
            throw new IllegalArgumentException("No negative slop allowed.");
        }
        this.slop = slop;
        return this;
    }

    /** Get the slop factor for phrase queries. */
    public int slop() {
        return this.slop;
    }
    
    @Override
    public String getName() {
        return NAME;
    }

    @Override
   	protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
   		Map<String,Object> queryMap = new HashMap<String,Object>();
   		queryMap.put(MatchQueryBuilder.QUERY_FIELD.getPreferredName(), value);
   		if (analyzer != null) {
   			queryMap.put(MatchQueryBuilder.ANALYZER_FIELD.getPreferredName(), analyzer);
   		}
   		queryMap.put(MatchPhraseQueryBuilder.SLOP_FIELD.getPreferredName(), slop);
   		printBoostAndQueryName(queryMap);
   		Map<String,Object> fieldMap = new HashMap<String,Object>();
   		fieldMap.put(fieldName, queryMap);
   		builder.put(NAME, fieldMap);
   		return builder;
   	}
	
    @Override
    protected boolean doEquals(MatchPhrasePrefixQueryBuilder other) {
        return Objects.equals(fieldName, other.fieldName) &&
                Objects.equals(value, other.value) &&
                Objects.equals(analyzer, other.analyzer)&&
                Objects.equals(slop, other.slop);
    }

    @Override
    protected int doHashCode() {
        return Objects.hash(fieldName, value, analyzer, slop);
    }

	
	
	
	
}
