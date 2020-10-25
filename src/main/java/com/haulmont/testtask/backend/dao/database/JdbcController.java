package com.haulmont.testtask.backend.dao.database;

import com.haulmont.testtask.backend.dao.exceptions.JdbcControllerException;
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
    public Connection connection;
    public Statement statement;

    /* database connection credentials initializing */
    private final ResourceBundle resource = ResourceBundle.getBundle("database");
    private final String jdbcSubprotocol = resource.getString("db.subprotocol");
    private final String dbPath = resource.getString("db.path");
    private final String dbLogin = resource.getString("db.login");
    private final String dbPassword = resource.getString("db.password");

    JdbcController() throws JdbcControllerException {
        try {
            Class.forName(resource.getString("db.driver"));
            connection = DriverManager.getConnection(getConnectionString(), dbLogin, dbPassword);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController constructor method \n" +
                    e.getMessage(), e);
        }
    }

    private static class JdbcControllerHolder {
        public static JdbcController instance = null;
        static {
            try {
                instance = new JdbcController();
            } catch (JdbcControllerException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static JdbcController getInstance() {
        return JdbcControllerHolder.instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    private String getConnectionString() {
        return "jdbc:" + jdbcSubprotocol + ":" + dbPath;
    }

    void disconnect() throws JdbcControllerException{
        try {
            connection.close();
        } catch (SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController.disconnect() method \n" +
                    e.getMessage(), e);
        }
    }
    // Выполнить SELECT
    public synchronized ResultSet executeQuery(String sql) throws JdbcControllerException{
        ResultSet rs = null;

        try {
            rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController.executeQuery() method \n" +
                    e.getMessage(), e);
        }

        return rs;
    }
    // Выполнить INSERT

    synchronized void executeUpdate(String sql) throws JdbcControllerException{
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController.executeUpdate() method \n" +
                    e.getMessage(), e);
        }
    }


    synchronized boolean execute(String sql) throws JdbcControllerException{
        boolean result = false;
        try {
            result = statement.execute(sql);
        } catch (SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController.execute() method \n" +
                    e.getMessage(), e);
        }
        return result;
    }

    ResultSet getResultSet() throws JdbcControllerException {
        ResultSet rs = null;
        try {
            rs = statement.getResultSet();
        } catch (SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController.getResultSet() method \n" +
                    e.getMessage(), e);
        }
        return rs;
    }

    synchronized void addBatch(String sql) throws JdbcControllerException{
        try {
            statement.addBatch(sql);
        } catch (SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController.addBatch() method \n" +
                    e.getMessage(), e);
        }
    }

    synchronized void executeBatch() throws JdbcControllerException{
        try {
            statement.executeBatch();
        } catch (SQLException e) {
            throw new  JdbcControllerException("Error in JdbcController.executeBatch() method \n" +
                    e.getMessage(), e);
        }
    }

}
