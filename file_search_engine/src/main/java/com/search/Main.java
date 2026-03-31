package com.search;
import com.search.db.DatabaseManager;
import com.search.db.SQLiteDatabaseManager;
import com.search.sync.FileCrawler;
import com.search.sync.SimpleFileCrawler;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import java.nio.file.Path;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        FileCrawler crawler = new SimpleFileCrawler();

        Set<String> excluded = Set.of(".jpg", ".png", ".exe", ".ogg");

        var files = crawler.crawl(
                Path.of("C:/Users/Tudor/OneDrive - Technical University of Cluj-Napoca/Desktop/random"),
                excluded
        );

        files.forEach(System.out::println);
    }
}

