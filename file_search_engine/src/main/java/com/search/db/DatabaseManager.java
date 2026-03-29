package com.search.db;

import java.sql.Connection;

public interface DatabaseManager {
    void connect();
    Connection getConnection();
    void initializeSchema();
    void close();
}
