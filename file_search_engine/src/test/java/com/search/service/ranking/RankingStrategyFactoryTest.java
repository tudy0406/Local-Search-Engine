package com.search.service.ranking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RankingStrategyFactoryTest {

    // -----------------------------
    // defaultStrategy()
    // -----------------------------

    @Test
    void defaultStrategy_shouldReturnScoreBased() {
        RankingStrategy strategy = RankingStrategyFactory.defaultStrategy();

        assertNotNull(strategy);
        assertTrue(strategy instanceof ScoreBasedRankingStrategy);
    }

    // -----------------------------
    // create(Type)
    // -----------------------------

    @Test
    void create_shouldReturnCorrectStrategy_forEachType() {
        assertInstanceOf(ScoreBasedRankingStrategy.class,
                RankingStrategyFactory.create(RankingStrategyFactory.Type.DEFAULT));

        assertInstanceOf(AlphabeticalRankingStrategy.class,
                RankingStrategyFactory.create(RankingStrategyFactory.Type.ALPHABETICAL));

        assertInstanceOf(PathLengthRankingStrategy.class,
                RankingStrategyFactory.create(RankingStrategyFactory.Type.PATH_LENGTH));

        assertInstanceOf(LastSearchedRankingStrategy.class,
                RankingStrategyFactory.create(RankingStrategyFactory.Type.LAST_SEARCHED));

        assertInstanceOf(LastAccessedDateRankingStrategy.class,
                RankingStrategyFactory.create(RankingStrategyFactory.Type.LAST_ACCESSED));

        assertInstanceOf(LastModifiedRankingStrategy.class,
                RankingStrategyFactory.create(RankingStrategyFactory.Type.LAST_MODIFIED));
    }

    // -----------------------------
    // fromName(String)
    // -----------------------------

    @Test
    void fromName_shouldHandleExactEnumNames() {
        RankingStrategy strategy = RankingStrategyFactory.fromName("ALPHABETICAL");

        assertInstanceOf(AlphabeticalRankingStrategy.class, strategy);
    }

    @Test
    void fromName_shouldHandleLowercaseAndHyphen() {
        RankingStrategy strategy = RankingStrategyFactory.fromName("last-searched");

        assertInstanceOf(LastSearchedRankingStrategy.class, strategy);
    }

    @Test
    void fromName_shouldHandleMixedCaseAndSpaces() {
        RankingStrategy strategy = RankingStrategyFactory.fromName("  Last_Accessed  ");

        assertInstanceOf(LastAccessedDateRankingStrategy.class, strategy);
    }

    @Test
    void fromName_shouldFallbackToDefault_onInvalidName() {
        RankingStrategy strategy = RankingStrategyFactory.fromName("invalid-strategy");

        assertInstanceOf(ScoreBasedRankingStrategy.class, strategy);
    }

    @Test
    void fromName_shouldFallbackToDefault_onNull() {
        RankingStrategy strategy = RankingStrategyFactory.fromName(null);

        assertInstanceOf(ScoreBasedRankingStrategy.class, strategy);
    }

    @Test
    void fromName_shouldFallbackToDefault_onBlank() {
        RankingStrategy strategy = RankingStrategyFactory.fromName("   ");

        assertInstanceOf(ScoreBasedRankingStrategy.class, strategy);
    }
}