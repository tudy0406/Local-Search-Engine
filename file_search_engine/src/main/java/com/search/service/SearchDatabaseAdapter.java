package com.search.service;

import com.search.db.DatabaseManager;
import com.search.model.FileData;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SearchDatabaseAdapter {

    private final DatabaseManager dbManager;

    public SearchDatabaseAdapter(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<FileData> search(String query) {

        List<FileData> results = new ArrayList<>();

        // FTS query (search in path, filename, content)
        String sql = """
            SELECT f.path, f.filename, f.size, f.modified_at, f.extension, f.content
            FROM files f
            JOIN files_fts fts ON f.id = fts.rowid
            WHERE files_fts MATCH ?
            LIMIT 50;
        """;

        try {
            Connection conn = dbManager.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, query);

                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {

                        FileData data = new FileData(
                                Path.of(rs.getString("path")),
                                rs.getString("content"),
                                rs.getLong("size"),
                                rs.getLong("modified_at"),
                                rs.getString("extension")
                        );

                        results.add(data);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Search error: " + e.getMessage());
        }

        return results;
    }
}