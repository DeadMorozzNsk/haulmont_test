package com.haulmont.testtask.dao.database;

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.domain.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class JdbcTest {
    @Test
    public void connectToDB() throws DaoException {
        JdbcDao jdbcController = null;
        try {
            jdbcController = new JdbcController();
        } catch (JdbcControllerException e) {
            e.printStackTrace();
        }
        Connection connection = jdbcController.getConnection();
        Assertions.assertNotNull(connection);

        List<Patient> list = DaoFactory.getInstance().getDaoPatient().getAll();
        Assertions.assertEquals(list, DaoFactory.getInstance().getDaoPatient().getAll());
        Assertions.assertEquals(list, DaoFactory.getInstance().getDaoDoctor().getAll());
    }
}
