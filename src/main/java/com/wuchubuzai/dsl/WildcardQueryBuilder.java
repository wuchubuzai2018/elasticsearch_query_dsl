package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implements the wildcard search query. Supported wildcards are <tt>*</tt>, which
 * matches any character sequence (including the empty one), and <tt>?</tt>,
 * which matches any single character. Note this query can be slow, as it
 * needs to iterate over many terms. In order to prevent extremely slow WildcardQueries,
 * a Wildcard term should not start with one of the wildcards <tt>*</tt> or
 * <tt>?</tt>.
 */
public class WildcardQueryBuilder extends AbstractQueryBuilder<WildcardQueryBuilder> implements MultiTermQueryBuilder {

    public static final String NAME = "wildcard";

    private static final ParseField WILDCARD_FIELD = new ParseField("wildcard");
    private static final ParseField VALUE_FIELD = new ParseField("value");
    private static final ParseField REWRITE_FIELD = new ParseField("rewrite");

    private final String fieldName;

    private final String value;

    private String rewrite;
    
    /**
     * Implements the wildcard search query. Supported wildcards are <tt>*</tt>, which
     * matches any character sequence (including the empty one), and <tt>?</tt>,
     * which matches any single character. Note this query can be slow, as it
     * needs to iterate over many terms. In order to prevent extremely slow WildcardQueries,
     * a Wildcard term should not start with one of the wildcards <tt>*</tt> or
     * <tt>?</tt>.
     *
     * @param fieldName The field name
     * @param value The wildcard query string
     */
    public WildcardQueryBuilder(String fieldName, String value) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.fieldName = fieldName;
        this.value = value;
    }
    
    public String fieldName() {
        return fieldName;
    }

    public String value() {
        return value;
    }

    public WildcardQueryBuilder rewrite(String rewrite) {
        this.rewrite = rewrite;
        return this;
    }

    public String rewrite() {
        return this.rewrite;
    }
	
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected int doHashCode() {
		return Objects.hash(fieldName, value, rewrite);
	}

	@Override
	protected boolean doEquals(WildcardQueryBuilder other) {
		return Objects.equals(fieldName, other.fieldName) && Objects.equals(value, other.value)
				&& Objects.equals(rewrite, other.rewrite);
	}

	@Override
	protected Map<String, Object> getXContent(Map<String, Object> builder) throws IOException {
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put(WILDCARD_FIELD.getPreferredName(), value);
		if (rewrite != null) {
			fieldMap.put(REWRITE_FIELD.getPreferredName(), rewrite);
		}
		printBoostAndQueryName(fieldMap);

		Map<String, Object> nameMap = new HashMap<String, Object>();
		nameMap.put(fieldName, fieldMap);

		builder.put(NAME, nameMap);
        return builder;
	}

}
