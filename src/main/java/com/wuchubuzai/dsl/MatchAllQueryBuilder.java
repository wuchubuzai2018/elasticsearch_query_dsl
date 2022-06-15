package com.wuchubuzai.dsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A query that matches on all documents.
 */
public class MatchAllQueryBuilder extends AbstractQueryBuilder<MatchAllQueryBuilder> {

	public static final String NAME = "match_all";

	@Override
	protected boolean doEquals(MatchAllQueryBuilder other) {
		return true;
	}

	@Override
	protected int doHashCode() {
		return 0;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	protected Map<String,Object> getXContent(Map<String,Object> builder) throws IOException {
		Map<String,Object> nestMap = new HashMap<String,Object>();
		printBoostAndQueryName(nestMap);
		builder.put(NAME, nestMap);
		return builder;
	}

}
