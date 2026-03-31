package com.search.sync;

import com.search.db.DatabaseManager;
import com.search.model.FileData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseAdapter{

    private final DatabaseManager dbManager;

    public DatabaseAdapter(DatabaseManager databaseManager) {
        this.dbManager = databaseManager;
    }

    public void insertFile(FileData file) {

        String sql = """
            INSERT OR REPLACE INTO files
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
            System.out.println(e.getMessage());
        }
    }
}
