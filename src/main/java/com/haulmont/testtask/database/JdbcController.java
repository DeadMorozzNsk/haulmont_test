package com.haulmont.testtask.database;

import lombok.Cleanup;

import java.sql.*;
import java.util.ResourceBundle;


public class JdbcController {
    private static JdbcController instance;
    public Connection connection;
    public Statement statement;
    /* database connection credentials initializing */

    private final ResourceBundle resource = ResourceBundle.getBundle("database");
    private final String dbDriverName = resource.getString("db.driver");
    private final String jdbcSubprotocol = resource.getString("db.subprotocol");
    private final String dbPath = resource.getString("db.path");
    private final String dbLogin = resource.getString("db.login");
    private final String dbPassword = resource.getString("db.password");

    public JdbcController() {
        try {
            Class.forName(dbDriverName);
            connection = DriverManager.getConnection(getConnectionString());
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (instance==null) instance = new JdbcController();
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    private String getConnectionString() {
        return "jdbc:" + jdbcSubprotocol + ":" + dbPath;
    }

//    private static class JdbcControllerHolder {
//        public static JdbcController instance = new JdbcController();
//    }
//
//    public static JdbcController getJdbc() {
//        return JdbcControllerHolder.instance;
//    }

    // Отключиться от базы
    public void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Выполнить SELECT
    public synchronized ResultSet executeQuery(String sql) {
        ResultSet rs = null;

        try {
            rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    // Выполнить INSERT
    public synchronized void executeUpdate(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized void execute(String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
