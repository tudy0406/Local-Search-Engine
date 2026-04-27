package com.search.service;

import com.search.model.FileData;
import com.search.service.ranking.RankingStrategy;
import com.search.sync.DatabaseAdapter;

import java.util.List;

public class SearchService {

    private final QueryParser parser;
    private final SearchDatabaseAdapter db;
    private final ResultFormatter formatter;
    private RankingStrategy rankingStrategy;
    private final DatabaseAdapter syncDatabaseAdapter;

    public SearchService(QueryParser parser,
                         SearchDatabaseAdapter db,
                         ResultFormatter formatter,
                         RankingStrategy rankingStrategy,
                         DatabaseAdapter syncDatabaseAdapter) {
        this.parser = parser;
        this.db = db;
        this.formatter = formatter;
        this.rankingStrategy = rankingStrategy;
        this.syncDatabaseAdapter = syncDatabaseAdapter;
    }

    public void search(String rawQuery) {

        String parsedQuery = parser.parse(rawQuery);

        if (parsedQuery.isEmpty()) {
            System.out.println("Empty query.");
            return;
        }

        List<FileData> results = db.search(parsedQuery, rankingStrategy);

        formatter.printResults(results);

        for(FileData fileData : results) {
            fileData.setLastSearched(System.currentTimeMillis());
            syncDatabaseAdapter.update(fileData);
        }
    }

    public void setRankingStrategy(RankingStrategy rankingStrategy) {
        this.rankingStrategy = rankingStrategy;
    }
}