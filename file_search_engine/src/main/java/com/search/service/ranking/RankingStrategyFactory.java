package com.search.service.ranking;

import java.util.Locale;

/**
 * Decouples the rest of the app from concrete strategy classes.
 * Adding a new strategy is one line here plus the new class itself.
 *
 * The {@link Type#DEFAULT} blends last-searched (heaviest) +
 * last-accessed + last-modified. Every other type is a strict
 * single-factor ordering chosen explicitly by the user.
 */
public final class RankingStrategyFactory {

    public enum Type {
        /** Combined recency: last-searched (heaviest) + last-accessed + last-modified. */
        DEFAULT,
        ALPHABETICAL,
        PATH_LENGTH,
        LAST_SEARCHED,
        LAST_ACCESSED,
        LAST_MODIFIED
    }

    private RankingStrategyFactory() {}

    public static RankingStrategy defaultStrategy() {
        return new ScoreBasedRankingStrategy();
    }

    public static RankingStrategy create(Type type) {
        return switch (type) {
            case DEFAULT       -> new ScoreBasedRankingStrategy();
            case ALPHABETICAL  -> new AlphabeticalRankingStrategy();
            case PATH_LENGTH   -> new PathLengthRankingStrategy();
            case LAST_SEARCHED -> new LastSearchedRankingStrategy();
            case LAST_ACCESSED -> new LastAccessedDateRankingStrategy();
            case LAST_MODIFIED -> new LastModifiedRankingStrategy();
        };
    }

    /** Accepts "last-searched", "LAST_SEARCHED", etc. Falls back to default. */
    public static RankingStrategy fromName(String name) {
        if (name == null || name.isBlank()) return defaultStrategy();
        try {
            return create(Type.valueOf(
                    name.trim().toUpperCase(Locale.ROOT).replace('-', '_')));
        } catch (IllegalArgumentException ex) {
            return defaultStrategy();
        }
    }
}
