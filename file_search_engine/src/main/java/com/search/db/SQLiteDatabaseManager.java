package com.search.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
                    id INTEGER PRIMARY KEY,
                    path TEXT UNIQUE,
                    filename TEXT,
                    extension TEXT,
                    size INTEGER,
                    modified_at Date,
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
                  INSERT INTO files_fts(rowid, path, filename, content)
                  VALUES (new.id, new.path, new.filename, new.content);
                END;
            """);
            stmt.execute("""
               CREATE TRIGGER IF NOT EXISTS files_ad AFTER DELETE ON files BEGIN
                 DELETE FROM files_fts WHERE rowid = old.id;
               END;
            """);
            stmt.execute("""
                CREATE TRIGGER IF NOT EXISTS files_au AFTER UPDATE ON files BEGIN
                  UPDATE files_fts
                  SET path = new.path,
                      filename = new.filename,
                      content = new.content
                  WHERE rowid = old.id;
                END;
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
            e.printStackTrace();
        }
    }
}