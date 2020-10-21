package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Priority;

import java.sql.*;

public class DaoPriority extends DaoEntity<Priority> {

    @Override
    protected Priority getEntity(ResultSet rs) throws DaoException {
        return null;
    }

    @Override
    protected ResultSet getAllResultSet(Statement statement) throws SQLException {
        return statement.executeQuery("SELECT * FROM PRIORITIES");
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Connection connection, Object paramEntity) throws SQLException {
        return null;
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Connection connection, Object paramEntity) throws SQLException {
        return null;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Connection connection, Object paramEntityId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM PATIENTS WHERE ID = ?");
        try {
            preparedStatement.setLong(1, (Long) paramEntityId);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected void setValues(PreparedStatement stmt, Priority entity) throws SQLException {

    }
}
