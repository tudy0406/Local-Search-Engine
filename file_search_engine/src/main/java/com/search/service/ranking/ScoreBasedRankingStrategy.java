package com.search.service.ranking;

/**
 * Default ranking strategy. Combines three recency signals into one
 * numeric score:
 *
 * <pre>
 *   score =  w_searched * recency(last_searched)
 *          + w_accessed * recency(last_accessed)
 *          + w_modified * recency(modified_at)
 *
 *   recency(t) = 1.0 / (1.0 + days_since(t))
 * </pre>
 *
 * Defaults: 0.55 last_searched + 0.30 last_accessed + 0.15 modified_at.
 * "Last searched" dominates because a file the user already pulled up
 * before is the strongest signal of ongoing interest.
 *
 * Each component is in [0, 1] so the weights alone determine relative
 * influence — none can swamp the others through raw timestamp magnitude.
 */
public class ScoreBasedRankingStrategy implements RankingStrategy {

    private final RankingWeights w;

    public ScoreBasedRankingStrategy() {
        this(RankingWeights.defaults());
    }

    public ScoreBasedRankingStrategy(RankingWeights weights) {
        this.w = weights;
    }

    @Override
    public String rank() {
        return "("
             + w.lastSearched + " * (1.0 / (1.0 + (strftime('%s','now')*1000 - last_searched)/86400000.0)) + "
             + w.lastAccessed + " * (1.0 / (1.0 + (strftime('%s','now')*1000 - last_accessed_at)/86400000.0)) + "
             + w.lastModified + " * (1.0 / (1.0 + (strftime('%s','now')*1000 - modified_at)/86400000.0))"
             + ")";
    }

    @Override
    public String name() { return "recency (default)"; }
}
