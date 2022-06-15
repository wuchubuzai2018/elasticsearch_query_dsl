package com.wuchubuzai.dsl;

public class MatchQuery {

	public enum Type {
		/**
		 * The text is analyzed and terms are added to a boolean query.
		 */
		BOOLEAN(0),
		/**
		 * The text is analyzed and used as a phrase query.
		 */
		PHRASE(1),
		/**
		 * The text is analyzed and used in a phrase query, with the last term
		 * acting as a prefix.
		 */
		PHRASE_PREFIX(2);

		@SuppressWarnings("unused")
		private final int ordinal;

		Type(int ordinal) {
			this.ordinal = ordinal;
		}
	}

	public enum ZeroTermsQuery {
		NONE(0), ALL(1);

		@SuppressWarnings("unused")
		private final int ordinal;

		ZeroTermsQuery(int ordinal) {
			this.ordinal = ordinal;
		}

	}
	
	 /**
     * the default phrase slop
     */
    public static final int DEFAULT_PHRASE_SLOP = 0;

    /**
     * the default leniency setting
     */
    public static final boolean DEFAULT_LENIENCY = false;

    /**
     * the default zero terms query
     */
    public static final ZeroTermsQuery DEFAULT_ZERO_TERMS_QUERY = ZeroTermsQuery.NONE;

}
