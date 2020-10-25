package com.haulmont.testtask.backend.dao.database;

import java.sql.Connection;
import java.sql.Statement;

public interface JdbcDao {

    Connection getConnection();
    Statement getStatement();
}
