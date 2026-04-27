package com.search;
import com.search.config.ConfigLoader;
import com.search.db.DatabaseManager;
import com.search.db.SQLiteDatabaseManager;
import com.search.report.IndexReport;
import com.search.service.QueryParser;
import com.search.service.ResultFormatter;
import com.search.service.SearchDatabaseAdapter;
import com.search.service.SearchService;
import com.search.service.ranking.*;
import com.search.sync.*;
import com.search.ui_cli.*;

public class Main {
    public static void main(String[] args) {

        DatabaseManager dbManager = new SQLiteDatabaseManager();
        dbManager.connect();
        dbManager.initializeSchema();

        // SYNC
        ConfigLoader configLoader = new ConfigLoader("C:/Users/Tudor/OneDrive - Technical University of Cluj-Napoca/Desktop/facultate/year_3/SD/file_search_engine_project/file_search_engine/src/main/java/com/search/config/app.properties");
        FileCrawler crawler = new SimpleFileCrawler();
        FileReader reader = new FileReader();
        MetadataExtractor extractor = new MetadataExtractor();
        DatabaseAdapter syncDb = new DatabaseAdapter(dbManager);

        SyncService syncService = new SyncService(configLoader, crawler, reader, extractor, syncDb);

        // SEARCH
        QueryParser parser = new QueryParser();
        SearchDatabaseAdapter searchDb = new SearchDatabaseAdapter(dbManager);
        ResultFormatter formatter = new ResultFormatter();
        RankingStrategy rankingStrategy = new ScoreBasedRankingStrategy();
        SearchService searchService = new SearchService(parser, searchDb, formatter, rankingStrategy, syncDb);

        // UI
        InputHandler input = new InputHandler();
        View view = new View();

        view.showWelcome();
        view.showIndexingStart();

        IndexReport indexReport = syncService.sync();
        indexReport.print();

        view.showRankingStrategy("ScoreBasedRankingStrategy");


        // MAIN LOOP
        while (true) {

            String query = input.readQuery();

            if (query.equalsIgnoreCase("exit")) {
                view.showExit();
                break;
            } else if (query.equals("rank")) {
                String rank = input.readRankingStrategy();
                searchService.setRankingStrategy(RankingStrategyFactory.fromName(rank));
                view.showRankingUpdated();
                continue;
            }

            if (query.isBlank()) {
                view.showEmptyQuery();
                continue;
            }

            searchService.search(query);
        }

        dbManager.close();
    }
}

