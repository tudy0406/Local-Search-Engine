package com.search.service.ranking;

/**
 * Strict ordering: filenames A → Z.
 *
 * Because the adapter applies {@code ORDER BY score DESC}, we have to
 * produce a numeric score where alphabetically-earlier filenames get
 * the <em>higher</em> value. We do that by negating the Unicode
 * code-points of the first five filename characters (lower-cased) and
 * placing them at descending decimal weights – effectively a
 * lexicographic comparison encoded as one number.
 *
 * "A..." → score ≈ -97e8, "Z..." → score ≈ -122e8;
 * sorted DESC, "A..." comes out on top.
 */
public class AlphabeticalRankingStrategy implements RankingStrategy {

    @Override
    public String rank() {
        return "(-(" +
                "COALESCE(UNICODE(SUBSTR(LOWER(f.filename), 1, 1)), 127) * 100000000.0 + " +
                "COALESCE(UNICODE(SUBSTR(LOWER(f.filename), 2, 1)), 127) * 1000000.0 + " +
                "COALESCE(UNICODE(SUBSTR(LOWER(f.filename), 3, 1)), 127) * 10000.0 + " +
                "COALESCE(UNICODE(SUBSTR(LOWER(f.filename), 4, 1)), 127) * 100.0 + " +
                "COALESCE(UNICODE(SUBSTR(LOWER(f.filename), 5, 1)), 127)" +
                "))";
    }

    @Override
    public String name() { return "alphabetical"; }
}
