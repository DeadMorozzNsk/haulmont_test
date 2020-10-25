package com.haulmont.testtask.backend.dao;

import com.haulmont.testtask.backend.dao.database.JdbcController;
import com.haulmont.testtask.backend.dao.exceptions.JdbcControllerException;
import com.haulmont.testtask.backend.domain.Priority;

import java.sql.*;

public class DaoPriority extends DaoEntity<Priority> {
    protected DaoEntityType type = DaoEntityType.DAO_PRIORITY;

    @Override
    protected Priority getEntity(ResultSet rs) throws SQLException {
        return new Priority( /* same order as declared in class due to Lombok annotation */
                rs.getLong("ID"),
                rs.getInt("PRIORITY"),
                rs.getString("NAME"));
    }

    @Override
    protected ResultSet getAllResultSet() throws JdbcControllerException {
        return JdbcController.getInstance().executeQuery("SELECT * FROM PRIORITIES");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Object paramEntity) throws SQLException {
        return getAddOrUpdateStatement(paramEntity,
                "INSERT INTO PRIORITIES (" +
                        "PRIORITY, " +
                        "NAME) VALUES (?, ?)");
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(paramEntity,
                "UPDATE PRIORITIES SET " +
                        "PRIORITY = ?, " +
                        "NAME = ?, WHERE ID = ?");
        ps.setLong(3, ((Priority) paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId, "DELETE FROM PRIORITIES WHERE ID = ?");
    }

    @Override
    protected PreparedStatement getByIdPrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId, "SELECT * FROM PRIORITIES WHERE ID = ?");
    }

    @Override
    protected void setValues(PreparedStatement stmt, Priority entity) throws SQLException {
        stmt.setInt(1, entity.getPriority());
        stmt.setString(2, entity.getName());
    }

    @Override
    public boolean deleteAvailable(long id) {
        return true;
    }
}