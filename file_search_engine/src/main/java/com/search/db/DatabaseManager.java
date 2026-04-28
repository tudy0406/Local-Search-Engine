package com.search.db;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface DatabaseManager {
    void connect();
    Connection getConnection();
    void initializeSchema();
    void close();
    Map<String, Integer> loadWordFrequencies() throws SQLException;
    Map<String, Integer> getFileWordCount(Path path) throws SQLException;
    void insertOrUpdateWord(String word, int count) throws SQLException;
    void insertFileWord(String path, String word, int count) throws SQLException;
    void deleteFileWords(String path) throws SQLException;

}
