package com.search.service.ranking;

/**
 * Strict ordering: most-recently-modified file first.
 * score = 1 / (1 + days since modified_at).
 */
public class LastModifiedRankingStrategy implements RankingStrategy {

    @Override
    public String rank() {
        return "(1.0 / (1.0 + (strftime('%s','now')*1000 - modified_at)/86400000.0))";
    }

    @Override
    public String name() { return "last-modified"; }
}
