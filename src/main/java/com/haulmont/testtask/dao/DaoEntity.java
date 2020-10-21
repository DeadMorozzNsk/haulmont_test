package com.haulmont.testtask.dao;

import com.haulmont.testtask.dao.database.JdbcController;
import com.haulmont.testtask.dao.database.JdbcDao;
import com.haulmont.testtask.domain.Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoEntity<E extends Entity> {
    private static JdbcDao jdbcController = JdbcController.getInstance();

    protected abstract E getEntity(ResultSet rs) throws DaoException;

    protected abstract ResultSet getAllResultSet(Statement statement) throws SQLException;

    protected abstract PreparedStatement getUpdatePrepStatement(Connection connection, Object paramEntity) throws SQLException;

    protected abstract PreparedStatement getAddPrepStatement(Connection connection, Object paramEntity) throws SQLException;

    protected abstract PreparedStatement getDeletePrepStatement(Connection connection, Object paramEntityId) throws SQLException;

    protected abstract void setValues(PreparedStatement stmt, E entity) throws SQLException;

    public boolean add(E entity) {
        return getBoolResultQuery(entity, QueryType.ADD);
    }

    public boolean update(E entity) {
        return getBoolResultQuery(entity, QueryType.UPDATE);
    }

    public boolean delete(Long id) {
        return getBoolResultQuery(id, QueryType.DELETE);
    }

    public boolean getBoolResultQuery(Object obj, QueryType queryType) {
        boolean result;
        try {
            PreparedStatement ps = null;
            switch (queryType) {
                case ADD:
                    ps = getAddPrepStatement(jdbcController.getConnection(), obj);
                    break;
                case UPDATE:
                    ps = getUpdatePrepStatement(jdbcController.getConnection(), obj);
                    break;
                case DELETE:
                    ps = getDeletePrepStatement(jdbcController.getConnection(), obj);
                    break;
            }
            result = ps.execute();
        } catch (SQLException e) {
            //printSQLException(e);
            result = false;
        }
        return result;
    }

    public E castParameterToEntity(Object objectEntity) {
        E objCast = null;
        try {
            objCast = (E) objectEntity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return objCast;
    }

    public PreparedStatement getAddOrUpdateStatement(Connection conn, Object obj, String sql) throws SQLException {
        E docCast = castParameterToEntity(obj);
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        setValues(preparedStatement, docCast);
        return preparedStatement;
    }

    private enum QueryType {
        ADD,
        UPDATE,
        DELETE
    }

    List<E> getAll() throws DaoException {
        List<E> list = null;
        try (Connection connection = jdbcController.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = getAllResultSet(statement)) {
            list = new ArrayList<>();
            while (rs.next()) {
                list.add(getEntity(rs));
            }
        } catch (SQLException e) {
            //printSQLException(e);
        }
        return list;
    }


}
