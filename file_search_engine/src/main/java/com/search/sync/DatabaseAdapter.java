package com.search.sync;

import com.search.db.DatabaseManager;
import com.search.model.FileData;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseAdapter{

    private final DatabaseManager dbManager;

    public DatabaseAdapter(DatabaseManager databaseManager) {
        this.dbManager = databaseManager;
    }

    public void insert(FileData file) {
        String sql = """
        INSERT INTO files
        (path, filename, extension, size, modified_at, content)
        VALUES (?, ?, ?, ?, ?, ?);
    """;

        try{
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, file.getPath().toString());
            stmt.setString(2, file.getPath().getFileName().toString());
            stmt.setString(3, file.getExtension());
            stmt.setLong(4, file.getSize());
            stmt.setLong(5, file.getModifiedAt());
            stmt.setString(6, file.getContent());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting file: " + e.getMessage());
        }
    }

    public void update(FileData file) {
        String sql = """
        UPDATE files
        SET filename = ?, extension = ?, size = ?, modified_at = ?, content = ?
        WHERE path = ?;
    """;

        try{
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, file.getPath().getFileName().toString());
            stmt.setString(2, file.getExtension());
            stmt.setLong(3, file.getSize());
            stmt.setLong(4, file.getModifiedAt());
            stmt.setString(5, file.getContent());
            stmt.setString(6, file.getPath().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating file: " + file.getPath(), e);
        }
    }

    public void delete(Path path) {
        String sql = "DELETE FROM files WHERE path = ?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, path.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting file: " + path, e);
        }
    }

    public Optional<Long> getLastModified(Path path) {
        String sql = """
        SELECT modified_at FROM files
        WHERE path = ?;
    """;

        try{
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, path.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getLong("modified_at"));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching last modified: " + path, e);
        }
    }

    public boolean exists(Path path) {
        String sql = """
        SELECT 1 FROM files
        WHERE path = ?
        LIMIT 1;
    """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, path.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence: " + path, e);
        }
    }
}
