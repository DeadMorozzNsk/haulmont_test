package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Priority;

import java.sql.*;

public class DaoPriority extends DaoEntity<Priority> {

    @Override
    protected Priority getEntity(ResultSet rs) throws DaoException {
        try {
            return new Priority( /* same order as declared in class due to Lombok annotation */
                    rs.getLong("ID"),
                    rs.getInt("PRIORITY"),
                    rs.getString("NAME"));
        } catch (SQLException e) {
            throw new DaoException("Could not create \"Priority\" entity", e);
        }
    }

    @Override
    protected ResultSet getAllResultSet(Statement statement) throws SQLException {
        return statement.executeQuery("SELECT * FROM PRIORITIES");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "INSERT INTO PRIORITIES (" +
                        "PRIORITY, " +
                        "NAME) VALUES (?, ?)");
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "UPDATE PRIORITIES SET " +
                        "PRIORITY = ?, " +
                        "NAME = ?, WHERE ID = ?");
        ps.setLong(3, ((Priority) paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Connection connection, Object paramEntityId) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM PRIORITIES WHERE ID = ?");
        preparedStatement.setLong(1, (Long) paramEntityId);
        return preparedStatement;
    }

    @Override
    protected void setValues(PreparedStatement stmt, Priority entity) throws SQLException {
        stmt.setInt(1, entity.getPriority());
        stmt.setString(2, entity.getName());
    }
}
