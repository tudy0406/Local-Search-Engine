package com.search.service;

import com.search.model.FileData;
import com.search.service.observer.SearchObserver;
import com.search.service.observer.SearchTracker;
import com.search.service.ranking.RankingStrategy;
import com.search.service.spelling_correction.SpellCorrector;
import com.search.sync.DatabaseAdapter;

import java.util.List;

public class SearchService {

    private final QueryParser parser;
    private final SearchDatabaseAdapter db;
    private final ResultFormatter formatter;
    private RankingStrategy rankingStrategy;
    private final DatabaseAdapter syncDatabaseAdapter;
    private final SearchTracker tracker;
    private final SearchObserver observer;
    private final SpellCorrector spellCorrector;


    public SearchService(QueryParser parser,
                         SearchDatabaseAdapter db,
                         ResultFormatter formatter,
                         RankingStrategy rankingStrategy,
                         DatabaseAdapter syncDatabaseAdapter,
                         SearchTracker tracker,
                         SearchObserver observer,
                         SpellCorrector spellCorrector) {
        this.parser = parser;
        this.db = db;
        this.formatter = formatter;
        this.rankingStrategy = rankingStrategy;
        this.syncDatabaseAdapter = syncDatabaseAdapter;
        this.tracker = tracker;
        this.observer = observer;
        this.spellCorrector = spellCorrector;
    }

    public void search(String rawQuery) {
        String correctedQuery = spellCorrector.correctQuery(rawQuery);

        if (!correctedQuery.equals(rawQuery)) {
            System.out.println("\n------------------\nDid you mean: " + correctedQuery + " ?\n------------------\n");
        }

        String parsedQuery = parser.parse(correctedQuery);

        if (parsedQuery.isEmpty()) {
            System.out.println("Empty query.");
            return;
        }

        List<FileData> results = db.search(parsedQuery, rankingStrategy);

        formatter.printResults(results);
        tracker.notifyObservers(correctedQuery, results);
        for(FileData fileData : results) {
            fileData.setLastSearched(System.currentTimeMillis());
            syncDatabaseAdapter.updateLastSearched(fileData);
        }
    }

    public void suggest(String rawQuery) {
        List<String> suggestions = observer.suggest(rawQuery);
        System.out.println("Suggestions: ");
        if(!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                System.out.println(suggestion);
            }
        }else{
            System.out.println("No suggestions.");
        }
    }

    public void setRankingStrategy(RankingStrategy rankingStrategy) {
        this.rankingStrategy = rankingStrategy;
    }
}