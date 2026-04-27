package com.search.service.ranking;

/**
 * Strict ordering: most-recently-accessed file first.
 * score = 1 / (1 + days since last_accessed).
 */
public class LastAccessedDateRankingStrategy implements RankingStrategy {

    @Override
    public String rank() {
        return "(1.0 / (1.0 + (strftime('%s','now')*10000 - last_accessed_at)/86400000.0))";
    }

    @Override
    public String name() { return "last-accessed"; }
}
