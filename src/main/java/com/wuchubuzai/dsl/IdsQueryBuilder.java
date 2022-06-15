package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A query that will return only documents matching specific ids (and a type).
 */
public class IdsQueryBuilder extends AbstractQueryBuilder<IdsQueryBuilder> {

	public static final String NAME = "ids";

	private static final ParseField TYPE_FIELD = new ParseField("type");
	private static final ParseField VALUES_FIELD = new ParseField("values");

	private final Set<String> ids = new HashSet<>();

	private String[] types = Strings.EMPTY_ARRAY;

	/**
     * Creates a new IdsQueryBuilder with no types specified upfront
     */
    public IdsQueryBuilder() {
        // nothing to do
    }

    /**
     * Add types to query
     */
    public IdsQueryBuilder types(String... types) {
        if (types == null) {
            throw new IllegalArgumentException("[" + NAME + "] types cannot be null");
        }
        this.types = types;
        return this;
    }
	
    /**
     * Returns the types used in this query
     */
    public String[] types() {
        return this.types;
    }

    /**
     * Adds ids to the query.
     */
    public IdsQueryBuilder addIds(String... ids) {
        if (ids == null) {
            throw new IllegalArgumentException("[" + NAME + "] ids cannot be null");
        }
        Collections.addAll(this.ids, ids);
        return this;
    }
    
    /**
     * Returns the ids for the query.
     */
    public Set<String> ids() {
        return this.ids;
    }
    
    @Override
    protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
    		Map<String,Object> valueMap = new HashMap<String,Object>();
    		valueMap.put(VALUES_FIELD.getPreferredName(), ids);
    		valueMap.put(TYPE_FIELD.getPreferredName(), types);
        printBoostAndQueryName(valueMap);
        builder.put(NAME, valueMap);
        return builder;
    }
	
    @Override
    public String getName() {
        return NAME;
    }
	
    @Override
    protected int doHashCode() {
        return Objects.hash(ids, Arrays.hashCode(types));
    }

    @Override
    protected boolean doEquals(IdsQueryBuilder other) {
        return Objects.equals(ids, other.ids) &&
               Arrays.equals(types, other.types);
    }
	
	
	
	
	
	
	
	
	
	
	
}
