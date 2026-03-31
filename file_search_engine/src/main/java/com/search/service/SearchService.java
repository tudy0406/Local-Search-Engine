package com.search.service;

import com.search.model.FileData;

import java.util.List;

public class SearchService {

    private final QueryParser parser;
    private final SearchDatabaseAdapter db;
    private final ResultFormatter formatter;

    public SearchService(QueryParser parser,
                         SearchDatabaseAdapter db,
                         ResultFormatter formatter) {
        this.parser = parser;
        this.db = db;
        this.formatter = formatter;
    }

    public void search(String rawQuery) {

        String parsedQuery = parser.parse(rawQuery);

        if (parsedQuery.isEmpty()) {
            System.out.println("Empty query.");
            return;
        }

        List<FileData> results = db.search(parsedQuery);

        formatter.printResults(results);
    }
}