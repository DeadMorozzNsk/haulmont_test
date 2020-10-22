package com.haulmont.testtask.dao.database;

import java.sql.Connection;
import java.sql.Statement;

public interface JdbcDao {

    Connection getConnection();
    Statement getStatement();
}
