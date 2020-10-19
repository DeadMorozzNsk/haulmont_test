package com.haulmont.testtask.database;

import java.sql.*;

public class JdbcInteractor {
    JdbcController controller;

    public void connect() throws ClassNotFoundException, SQLException {
        if (controller==null) controller = new JdbcController();;
    }

    public void disconnect() throws SQLException {
        controller.disconnect();
    }

//    public static void main(String[] args) throws SQLException {
//        try {
//            connect();
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            long t = System.currentTimeMillis();
//            connection.setAutoCommit(false); //убираем автообновление базы
//            //stmt.execute("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='table_name'");
//
//            stmt.executeUpdate("INSERT INTO students(name, score)\n" +
//                    "VALUES('Bob', 130)");
//            //Savepoint sp1 = connection.setSavepoint(); //отключает autocommit
//            stmt.executeUpdate("INSERT INTO students(name, score)\n" +
//                    "VALUES('Leo', 100)");
//            //connection.rollback(sp1);
//            stmt.executeUpdate("INSERT INTO students(name, score)\n" +
//                    "VALUES('Scott', 90)");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
//            while (rs.next()) {
//                System.out.println(rs.getInt("score"));
//            }
//            connection.setAutoCommit(true);
//            System.out.println("Операция выполнена за " + (System.currentTimeMillis() - t) / 1000f + " с");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            disconnect();
//        }
//
//    }
}
