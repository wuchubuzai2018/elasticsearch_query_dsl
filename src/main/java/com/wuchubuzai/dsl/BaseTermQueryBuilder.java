package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseTermQueryBuilder<QB extends BaseTermQueryBuilder<QB>> extends AbstractQueryBuilder<QB> {

    public static final ParseField VALUE_FIELD = new ParseField("value");

    /** Name of field to match against. */
    protected final String fieldName;

    /** Value to find matches for. */
    protected final Object value;
	
    /**
     * Constructs a new base term query.
     *
     * @param fieldName  The name of the field
     * @param value The value of the term
     */
    public BaseTermQueryBuilder(String fieldName, String value) {
        this(fieldName, (Object) value);
    }

    /**
     * Constructs a new base term query.
     *
     * @param fieldName  The name of the field
     * @param value The value of the term
     */
    public BaseTermQueryBuilder(String fieldName, int value) {
        this(fieldName, (Object) value);
    }

    /**
     * Constructs a new base term query.
     *
     * @param fieldName  The name of the field
     * @param value The value of the term
     */
    public BaseTermQueryBuilder(String fieldName, long value) {
        this(fieldName, (Object) value);
    }

    /**
     * Constructs a new base term query.
     *
     * @param fieldName  The name of the field
     * @param value The value of the term
     */
    public BaseTermQueryBuilder(String fieldName, float value) {
        this(fieldName, (Object) value);
    }

    /**
     * Constructs a new base term query.
     *
     * @param fieldName  The name of the field
     * @param value The value of the term
     */
    public BaseTermQueryBuilder(String fieldName, double value) {
        this(fieldName, (Object) value);
    }

    /**
     * Constructs a new base term query.
     *
     * @param fieldName  The name of the field
     * @param value The value of the term
     */
    public BaseTermQueryBuilder(String fieldName, boolean value) {
        this(fieldName, (Object) value);
    }

    /**
     * Constructs a new base term query.
     * In case value is assigned to a string, we internally convert it to a {@link BytesRef}
     * because in {@link TermQueryBuilder} and {@link SpanTermQueryBuilder} string values are parsed to {@link BytesRef}
     * and we want internal representation of query to be equal regardless of whether it was created from XContent or via Java API.
     *
     * @param fieldName  The name of the field
     * @param value The value of the term
     */
    public BaseTermQueryBuilder(String fieldName, Object value) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.fieldName = fieldName;
        this.value = value;
    }
    
    /** Returns the field name used in this query. */
    public String fieldName() {
        return this.fieldName;
    }

    /**
     *  Returns the value used in this query.
     *  If necessary, converts internal {@link BytesRef} representation back to string.
     */
    public Object value() {
        return this.value;
    }

    
    @Override
	protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
    		Map<String,Object> fieldMap = new HashMap<String,Object>();
    		fieldMap.put(VALUE_FIELD.getPreferredName(), this.value);
    	
    		Map<String,Object> nestMap = new HashMap<String,Object>();
    		nestMap.put(fieldName, fieldMap);
    		
		printBoostAndQueryName(fieldMap);
		builder.put(getName(), nestMap);
		return builder;
    }
    
    @Override
    protected final int doHashCode() {
        return Objects.hash(fieldName, value);
    }

    @Override
    protected final boolean doEquals(BaseTermQueryBuilder other) {
        return Objects.equals(fieldName, other.fieldName) &&
               Objects.equals(value, other.value);
    }
	
	
}
