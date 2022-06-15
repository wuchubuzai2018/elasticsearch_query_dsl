package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public final class SearchSourceBuilder {
	
	public static final ParseField FROM_FIELD = new ParseField("from", new String[0]);
    public static final ParseField SIZE_FIELD = new ParseField("size", new String[0]);
    public static final ParseField QUERY_FIELD = new ParseField("query", new String[0]);
    public static final ParseField SORT_FIELD = new ParseField("sort", new String[0]);
    public static final ParseField HIGHLIGHT_FIELD = new ParseField("highlight", new String[0]);
    private QueryBuilder queryBuilder;
    private int from = -1;
    private int size = -1;
    public static SearchSourceBuilder searchSource() {
        return new SearchSourceBuilder();
    }
    public SearchSourceBuilder() {
    }
    public SearchSourceBuilder query(QueryBuilder query) {
        this.queryBuilder = query;
        return this;
    }
    public QueryBuilder query() {
        return this.queryBuilder;
    }
    
    public SearchSourceBuilder from(int from) {
        if (from < 0) {
            throw new IllegalArgumentException("[from] parameter cannot be negative");
        } else {
            this.from = from;
            return this;
        }
    }

    public int from() {
        return this.from;
    }

    public SearchSourceBuilder size(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("[size] parameter cannot be negative, found [" + size + "]");
        } else {
            this.size = size;
            return this;
        }
    }
    
    public int size() {
        return this.size;
    }
    
    public Map<String,Object> innerToXContent(Map<String,Object> builder) throws IOException {
        if (this.from != -1) {
            builder.put(FROM_FIELD.getPreferredName(), this.from);
        }

        if (this.size != -1) {
        	 builder.put(SIZE_FIELD.getPreferredName(), this.size);
        }

        if (this.queryBuilder != null) {
        	 builder.put(QUERY_FIELD.getPreferredName(), this.queryBuilder.toXContent(new HashMap<String,Object>()));
        }

        return builder;
    }
    
    
    
    public String toString() {
    	 try {
			 Map<String,Object> builder = new HashMap<String,Object>();
			 innerToXContent(builder);
			return JSON.toJSONString(builder,true);
		} catch (Exception e) {
            throw new RuntimeException("cannot generate error message for deserialization", e);

		}
    }

}
