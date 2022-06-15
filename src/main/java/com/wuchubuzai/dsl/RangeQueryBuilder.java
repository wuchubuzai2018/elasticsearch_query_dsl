package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A Query that matches documents within an range of terms.
 */
public class RangeQueryBuilder extends AbstractQueryBuilder<RangeQueryBuilder> implements MultiTermQueryBuilder {
    public static final String NAME = "range";

    public static final boolean DEFAULT_INCLUDE_UPPER = true;
    public static final boolean DEFAULT_INCLUDE_LOWER = true;

    public static final ParseField LTE_FIELD = new ParseField("lte");
    public static final ParseField GTE_FIELD = new ParseField("gte");
    public static final ParseField FROM_FIELD = new ParseField("from");
    public static final ParseField TO_FIELD = new ParseField("to");
    private static final ParseField INCLUDE_LOWER_FIELD = new ParseField("include_lower");
    private static final ParseField INCLUDE_UPPER_FIELD = new ParseField("include_upper");
    public static final ParseField GT_FIELD = new ParseField("gt");
    public static final ParseField LT_FIELD = new ParseField("lt");
    private static final ParseField TIME_ZONE_FIELD = new ParseField("time_zone");
    private static final ParseField FORMAT_FIELD = new ParseField("format");
    private static final ParseField RELATION_FIELD = new ParseField("relation");

    private final String fieldName;
    
    
    private Object from;

    private Object to;

//    private DateTimeZone timeZone;

    private boolean includeLower = DEFAULT_INCLUDE_LOWER;

    private boolean includeUpper = DEFAULT_INCLUDE_UPPER;

//    private FormatDateTimeFormatter format;

//    private ShapeRelation relation;
    
    
    /**
     * A Query that matches documents within an range of terms.
     *
     * @param fieldName The field name
     */
    public RangeQueryBuilder(String fieldName) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        this.fieldName = fieldName;
    }
    
    /**
     * Get the field name for this query.
     */
    public String fieldName() {
        return this.fieldName;
    }

    
    /**
     * The from part of the range query. Null indicates unbounded.
     * In case lower bound is assigned to a string, we internally convert it to a {@link BytesRef} because
     * in {@link RangeQueryBuilder} field are later parsed as {@link BytesRef} and we need internal representation
     * of query to be equal regardless of whether it was created from XContent or via Java API.
     */
    public RangeQueryBuilder from(Object from, boolean includeLower) {
        this.from = from;
        this.includeLower = includeLower;
        return this;
    }
    
    
    /**
     * The from part of the range query. Null indicates unbounded.
     */
    public RangeQueryBuilder from(Object from) {
        return from(from, this.includeLower);
    }

    /**
     * Gets the lower range value for this query.
     */
    public Object from() {
        return this.from;
    }

    /**
     * The from part of the range query. Null indicates unbounded.
     */
    public RangeQueryBuilder gt(Object from) {
        return from(from, false);
    }

    /**
     * The from part of the range query. Null indicates unbounded.
     */
    public RangeQueryBuilder gte(Object from) {
        return from(from, true);
    }

    /**
     * The to part of the range query. Null indicates unbounded.
     */
    public RangeQueryBuilder to(Object to, boolean includeUpper) {
        this.to = to;
        this.includeUpper = includeUpper;
        return this;
    }
    
    /**
     * The to part of the range query. Null indicates unbounded.
     */
    public RangeQueryBuilder to(Object to) {
        return to(to, this.includeUpper);
    }

    /**
     * Gets the upper range value for this query.
     * In case upper bound is assigned to a string, we internally convert it to a {@link BytesRef} because
     * in {@link RangeQueryBuilder} field are later parsed as {@link BytesRef} and we need internal representation
     * of query to be equal regardless of whether it was created from XContent or via Java API.
     */
    public Object to() {
        return this.to;
    }

    /**
     * The to part of the range query. Null indicates unbounded.
     */
    public RangeQueryBuilder lt(Object to) {
        return to(to, false);
    }

    /**
     * The to part of the range query. Null indicates unbounded.
     */
    public RangeQueryBuilder lte(Object to) {
        return to(to, true);
    }

    /**
     * Should the lower bound be included or not. Defaults to <tt>true</tt>.
     */
    public RangeQueryBuilder includeLower(boolean includeLower) {
        this.includeLower = includeLower;
        return this;
    }

    /**
     * Gets the includeLower flag for this query.
     */
    public boolean includeLower() {
        return this.includeLower;
    }

    /**
     * Should the upper bound be included or not. Defaults to <tt>true</tt>.
     */
    public RangeQueryBuilder includeUpper(boolean includeUpper) {
        this.includeUpper = includeUpper;
        return this;
    }

    /**
     * Gets the includeUpper flag for this query.
     */
    public boolean includeUpper() {
        return this.includeUpper;
    }

    
    @Override
    protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
    	
    		Map<String,Object> queryMap = new HashMap<String,Object>();
    	
    		queryMap.put(FROM_FIELD.getPreferredName(), this.from);
    		queryMap.put(TO_FIELD.getPreferredName(), this.to);
    		queryMap.put(INCLUDE_LOWER_FIELD.getPreferredName(), includeLower);
    		queryMap.put(INCLUDE_UPPER_FIELD.getPreferredName(), includeUpper);
         printBoostAndQueryName(queryMap);
    		Map<String,Object> fieldMap = new HashMap<String,Object>();
    		fieldMap.put(fieldName, queryMap);
    		
    		builder.put(NAME, fieldMap);
    	
    		return builder;
//        builder.startObject(NAME);
//        builder.startObject(fieldName);
//        builder.field(FROM_FIELD.getPreferredName(), this.from);
//        builder.field(TO_FIELD.getPreferredName(), this.to);
//        builder.field(INCLUDE_LOWER_FIELD.getPreferredName(), includeLower);
//        builder.field(INCLUDE_UPPER_FIELD.getPreferredName(), includeUpper);
//        if (timeZone != null) {
//            builder.field(TIME_ZONE_FIELD.getPreferredName(), timeZone.getID());
//        }
//        if (format != null) {
//            builder.field(FORMAT_FIELD.getPreferredName(), format.format());
//        }
//        if (relation != null) {
//            builder.field(RELATION_FIELD.getPreferredName(), relation.getRelationName());
//        }

//        builder.endObject();
//        builder.endObject();
    }
    
    @Override
    public String getName() {
        return NAME;
    }
    
    
    @Override
    protected int doHashCode() {
//        String timeZoneId = timeZone == null ? null : timeZone.getID();
//        String formatString = format == null ? null : format.format();
        return Objects.hash(fieldName, from, to, includeLower, includeUpper);
    }

    @Override
    protected boolean doEquals(RangeQueryBuilder other) {
       
        return Objects.equals(fieldName, other.fieldName) &&
               Objects.equals(from, other.from) &&
               Objects.equals(to, other.to) &&
               Objects.equals(includeLower, other.includeLower) &&
               Objects.equals(includeUpper, other.includeUpper);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
