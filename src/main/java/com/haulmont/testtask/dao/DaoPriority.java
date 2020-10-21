package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Priority;

import java.sql.*;

public class DaoPriority extends DaoEntity<Priority> {

    @Override
    protected Priority getEntity(ResultSet rs) throws DaoException {
        try {
            return Priority.valueOf(rs.getString("NAME"));
        } catch (SQLException e) {
            throw new DaoException("Could not create \"Priority\" entity", e);
        }
    }

    @Override
    protected ResultSet getAllResultSet(Statement statement) throws SQLException {
        return statement.executeQuery("SELECT * FROM PRIORITIES");
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Connection connection, Object paramEntity) {
        return null;
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Connection connection, Object paramEntity) {
        return null;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Connection connection, Object paramEntityId) {
        return null;
    }

    @Override
    protected void setValues(PreparedStatement stmt, Priority entity) {

    }
}
