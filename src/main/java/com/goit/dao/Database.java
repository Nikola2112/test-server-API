package com.goit.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5433/postgresDB";
    private static Database instance;
    private Database(){

    }
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e1) {
            e1.fillInStackTrace();
        }
        return DriverManager.getConnection(JDBC_URL, "postgres", "flash8898");
    }
}