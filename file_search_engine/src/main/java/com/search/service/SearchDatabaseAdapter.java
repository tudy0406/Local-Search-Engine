package com.search.service;

import com.search.db.DatabaseManager;
import com.search.model.FileData;
import com.search.service.ranking.RankingStrategy;

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

    public List<FileData> search(String query, RankingStrategy rankingStrategy) {

        List<FileData> results = new ArrayList<>();

        String sql = """
                SELECT f.path, f.filename, f.size, f.modified_at, f.last_accessed_at, f.last_searched, f.extension, f.content,
                """
                + rankingStrategy.rank() +
                """
                AS score
                FROM files f
                JOIN files_fts fts ON f.path = fts.path
                WHERE files_fts MATCH ?
                ORDER BY score DESC
                LIMIT 20
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
                                rs.getLong("last_accessed_at"),
                                rs.getLong("last_searched"),
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