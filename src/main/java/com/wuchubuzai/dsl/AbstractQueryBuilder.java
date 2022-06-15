package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;


public abstract class AbstractQueryBuilder<QB extends AbstractQueryBuilder<QB>> implements QueryBuilder {

    public static final float DEFAULT_BOOST = 1.0f;
    
    public static final ParseField NAME_FIELD = new ParseField("_name");
    public static final ParseField BOOST_FIELD = new ParseField("boost");
    
	protected String queryName;
	protected float boost = DEFAULT_BOOST;
	
	
	protected AbstractQueryBuilder() {

    }
	
	/**
     * Sets the query name for the query.
     */
    @SuppressWarnings("unchecked")
    @Override
    public final QB queryName(String queryName) {
        this.queryName = queryName;
        return (QB) this;
    }

    /**
     * Returns the query name for the query.
     */
    @Override
    public final String queryName() {
        return queryName;
    }

    /**
     * Returns the boost for this query.
     */
    @Override
    public final float boost() {
        return this.boost;
    }
    
    /**
     * Sets the boost for this query.  Documents matching this query will (in addition to the normal
     * weightings) have their score multiplied by the boost provided.
     */
    @SuppressWarnings("unchecked")
    @Override
    public final QB boost(float boost) {
        this.boost = boost;
        return (QB) this;
    }
    
    @Override
    public abstract String getName();
    protected abstract int doHashCode();
    
    protected abstract boolean doEquals(QB other);
    
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        QB other = (QB) obj;
        return Objects.equals(queryName, other.queryName) &&
                Objects.equals(boost, other.boost) &&
                doEquals(other);
    }

	 @Override
	 public final int hashCode() {
	        return Objects.hash(getClass(), queryName, boost, doHashCode());
	 }
	 
	 protected void printBoostAndQueryName(Map<String,Object> builder) throws IOException {
        builder.put(BOOST_FIELD.getPreferredName(), boost);
        if (queryName != null) {
            builder.put(NAME_FIELD.getPreferredName(), queryName);
        }
    }
	 
	protected final void checkNegativeBoost(float boost) {
	        if (Float.compare(boost, 0.0F) < 0) {
//	            deprecationLogger.deprecatedAndMaybeLog("negative boost", "setting a negative [boost] on a query is deprecated and will throw an error in the next major version. You can use a value between 0 and 1 to deboost.", new Object[0]);
	        }

	    }
	 
	@Override
	public Map<String,Object> toXContent(Map<String,Object> builder) throws IOException {
		getXContent(builder);
		return builder;
	}
	 
	 protected abstract Map<String,Object> getXContent(Map<String,Object> builder) throws IOException;
	 
	 @Override
	 public final String toString(){
		 try {
			 Map<String,Object> builder = new HashMap<String,Object>();
			 getXContent(builder);
			return JSON.toJSONString(builder,true);
		} catch (IOException e) {
			try {
				Map<String,Object> builder = new HashMap<String,Object>();
                builder.put("error", "error building toString out of XContent: " + e.getMessage());
                builder.put("stack_trace", ExceptionsHelper.stackTrace(e));
                return JSON.toJSONString(builder,true);
            } catch (Exception e2) {
                throw new RuntimeException("cannot generate error message for deserialization", e);
            }
		}
	 }

	
	
}
