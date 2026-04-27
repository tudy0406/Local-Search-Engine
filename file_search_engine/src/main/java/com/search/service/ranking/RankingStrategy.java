package com.search.service.ranking;

public interface RankingStrategy {

    String rank();

    default String name() {
        return getClass().getSimpleName();
    }
}
