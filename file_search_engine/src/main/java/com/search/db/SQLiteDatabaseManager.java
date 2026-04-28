package com.search.db;

import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLiteDatabaseManager implements DatabaseManager {

    private static final String URL = "jdbc:sqlite:C:/Users/Tudor/OneDrive - Technical University of Cluj-Napoca/Desktop/facultate/year_3/SD/file_search_engine_project/file_search_engine/search.db";
    private Connection connection;

    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connected to SQLite.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void initializeSchema() {
        System.out.println(">>> initializeSchema called");
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS files (
                    path TEXT PRIMARY KEY,
                    filename TEXT,
                    extension TEXT,
                    size INTEGER,
                    modified_at INTEGER,
                    last_accessed_at INTEGER,
                    last_searched INTEGER,
                    content TEXT
                );
            """);

            stmt.execute("""
                CREATE VIRTUAL TABLE IF NOT EXISTS files_fts USING fts5(
                    path,
                    filename,
                    content
                );
            """);

            stmt.execute("""
                CREATE TRIGGER IF NOT EXISTS files_ai AFTER INSERT ON files BEGIN
                  INSERT INTO files_fts(path, filename, content)
                  VALUES (new.path, new.filename, new.content);
                END;
            """);
            stmt.execute("""
               CREATE TRIGGER IF NOT EXISTS files_ad AFTER DELETE ON files BEGIN
                 DELETE FROM files_fts WHERE path = old.path;
               END;
            """);
            stmt.execute("""
                CREATE TRIGGER IF NOT EXISTS files_au AFTER UPDATE ON files BEGIN
                  UPDATE files_fts
                  SET filename = new.filename,
                      content = new.content
                  WHERE path = old.path;
                END;
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS word_frequency (
                    word TEXT PRIMARY KEY,
                    count INTEGER NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS file_word_count (
                    file_path TEXT,
                    word TEXT,
                    count INTEGER,
                    PRIMARY KEY (file_path, word)
                );
            """);

            stmt.execute("""
            CREATE INDEX IF NOT EXISTS idx_file_word_path ON file_word_count(file_path);
            CREATE INDEX IF NOT EXISTS idx_word_frequency_word ON word_frequency(word);
            """);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing connection.");
        }
    }

    public Map<String, Integer> loadWordFrequencies() throws SQLException {
        Map<String, Integer> wordFreq = new HashMap<>();

        String sql = "SELECT word, count FROM word_frequency";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String word = rs.getString("word");
                int count = rs.getInt("count");

                wordFreq.put(word, count);
            }
        }

        return wordFreq;
    }

    public Map<String, Integer> getFileWordCount(Path path) throws SQLException {
        Map<String, Integer> wordFreq = new HashMap<>();

        String sql = "SELECT word, count FROM file_word_count WHERE file_path = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(path));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String word = rs.getString("word");
                    int count = rs.getInt("count");

                    wordFreq.put(word, count);
                }
            }
        }

        return wordFreq;
    }



    public void insertOrUpdateWord(String word, int delta) throws SQLException {
        String sql = """
        INSERT INTO word_frequency(word, count)
        VALUES (?, ?)
        ON CONFLICT(word)
        DO UPDATE SET count = count + excluded.count
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, word);
            stmt.setInt(2, delta);
            stmt.executeUpdate();
        }

        String cleanup = "DELETE FROM word_frequency WHERE count <= 0";

        try (PreparedStatement stmt = connection.prepareStatement(cleanup)) {
            stmt.executeUpdate();
        }
    }

    public void insertFileWord(String path, String word, int count) throws SQLException {
        String sql = """
        INSERT INTO file_word_count(file_path, word, count)
        VALUES (?, ?, ?)
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, path);
            stmt.setString(2, word);
            stmt.setInt(3, count);
            stmt.executeUpdate();
        }
    }

    public void deleteFileWords(String path) throws SQLException {
        String sql = "DELETE FROM file_word_count WHERE file_path = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, path);
            stmt.executeUpdate();
        }
    }
}