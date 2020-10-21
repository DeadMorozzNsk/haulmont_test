package com.haulmont.testtask.dao.database;

import java.sql.Connection;

public interface JdbcDao {
    void connect();

    Connection getConnection();
}
