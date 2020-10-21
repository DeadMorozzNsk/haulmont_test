package com.haulmont.testtask.dao.database;

import lombok.Getter;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * Class
 *
 * @author Anton Evlampeev
 */
@Getter
public class JdbcController implements JdbcDao {
    private static JdbcController instance;
    public Connection connection;
    public Statement statement;

    /* database connection credentials initializing */
    private final ResourceBundle resource = ResourceBundle.getBundle("database");
    private final String jdbcSubprotocol = resource.getString("db.subprotocol");
    private final String dbPath = resource.getString("db.path");
    private final String dbLogin = resource.getString("db.login");
    private final String dbPassword = resource.getString("db.password");

    JdbcController() {
        try {
            Class.forName(resource.getString("db.driver"));
            connection = DriverManager.getConnection(getConnectionString());
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized JdbcDao getInstance(){
        if (instance==null) instance = new JdbcController();
        return instance;
    }

    @Override
    public void connect() {
        getConnection();
    }

    public Connection getConnection() {
        if (instance == null) instance = new JdbcController();
        return connection;
    }

    Statement getStatement() {
        return statement;
    }

    private String getConnectionString() {
        return "jdbc:" + jdbcSubprotocol + ":" + dbPath;
    }
    // Отключиться от базы

    void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Выполнить SELECT

    synchronized ResultSet executeQuery(String sql) {
        ResultSet rs = null;

        try {
            rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }
    // Выполнить INSERT

    synchronized void executeUpdate(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    synchronized boolean execute(String sql) {
        boolean result = false;
        try {
           result = statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    ResultSet getResultSet() throws JdbcControllerException {
        ResultSet rs = null;
        try {
            rs = statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    synchronized void addBatch(String sql) {
        try {
            statement.addBatch(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    synchronized void executeBatch() {
        try {
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
