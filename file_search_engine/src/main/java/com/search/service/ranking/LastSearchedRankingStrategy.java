package com.search.service.ranking;

/**
 * Strict ordering: most-recently-searched file first.
 * score = 1 / (1 + days since last_searched).
 */
public class LastSearchedRankingStrategy implements RankingStrategy {

    @Override
    public String rank() {
        return "(1.0 / (1.0 + (strftime('%s','now')*1000 - last_searched)/86400000.0))";
    }

    @Override
    public String name() { return "last-searched"; }
}
