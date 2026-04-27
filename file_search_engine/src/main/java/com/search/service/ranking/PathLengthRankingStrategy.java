package com.search.service.ranking;

/**
 * Strict ordering: shortest absolute path first.
 * score = 1 / (1 + length(path))  →  shorter path → higher score.
 */
public class PathLengthRankingStrategy implements RankingStrategy {

    @Override
    public String rank() {
        return "(1.0 / (1.0 + LENGTH(f.path)))";
    }

    @Override
    public String name() { return "path-length"; }
}
