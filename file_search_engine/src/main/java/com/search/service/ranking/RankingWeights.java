package com.search.service.ranking;

/**
 * Weights for the default combined-recency strategy
 * ({@link ScoreBasedRankingStrategy}). Injected verbatim into the SQL
 * score expression at construction time.
 *
 * "Last searched" carries the largest weight because it captures direct
 * prior interest in the file from this search engine; last-accessed and
 * last-modified are secondary and tertiary signals.
 */
public final class RankingWeights {

    public final double lastSearched;
    public final double lastAccessed;
    public final double lastModified;

    public RankingWeights(double lastSearched, double lastAccessed, double lastModified) {
        this.lastSearched = lastSearched;
        this.lastAccessed = lastAccessed;
        this.lastModified = lastModified;
    }

    /** 0.55 / 0.30 / 0.15  (sum = 1.0) */
    public static RankingWeights defaults() {
        return new RankingWeights(0.55, 0.30, 0.15);
    }
}
