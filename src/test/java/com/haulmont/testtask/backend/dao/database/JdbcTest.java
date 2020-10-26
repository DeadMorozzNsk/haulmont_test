package com.haulmont.testtask.backend.dao.database;

import com.haulmont.testtask.backend.dao.exceptions.JdbcControllerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;


public class JdbcTest {
    @Test
    public void connectToDB(){
        JdbcDao jdbcController = null;
        try {
            jdbcController = new JdbcController();
        } catch (JdbcControllerException e) {
            e.printStackTrace();
        }
        Connection connection = jdbcController.getConnection();
        Assertions.assertNotNull(connection);
    }
}