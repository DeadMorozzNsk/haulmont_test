package com.haulmont.testtask.dao;

import com.haulmont.testtask.dao.database.JdbcController;
import com.haulmont.testtask.dao.database.JdbcControllerException;
import com.haulmont.testtask.dao.database.JdbcDao;
import com.haulmont.testtask.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoEntity<T extends Entity> {
    private static JdbcDao jdbcController = JdbcController.getInstance();
    private enum QueryType {
        ADD,
        UPDATE,
        DELETE
    }

    protected abstract T getEntity(ResultSet rs) throws SQLException;

    protected abstract ResultSet getAllResultSet() throws JdbcControllerException;

    protected abstract PreparedStatement getUpdatePrepStatement(Object paramEntity) throws SQLException;

    protected abstract PreparedStatement getAddPrepStatement(Object paramEntity) throws SQLException;

    protected abstract PreparedStatement getDeletePrepStatement(Object paramEntityId) throws SQLException;

    protected abstract PreparedStatement getByIdPrepStatement(Object paramEntityId) throws SQLException;

    protected abstract void setValues(PreparedStatement stmt, T entity) throws SQLException;

    protected abstract boolean deleteAvailable(long id) throws SQLException;

    /*protected abstract boolean deleteAvailable(long id);*/

    public boolean add(T entity) throws DaoException {
        return getBoolResultQuery(entity, QueryType.ADD);
    }

    public boolean update(T entity) throws DaoException {
        return getBoolResultQuery(entity, QueryType.UPDATE);
    }

    public boolean delete(Long id) throws DaoException {
        /*if (!deleteAvailable(id)) return false*/
        return getBoolResultQuery(id, QueryType.DELETE);
    }

    protected boolean getBoolResultQuery(Object obj, QueryType queryType) throws DaoException{
        boolean result;
        try {
            PreparedStatement ps = null;
            switch (queryType) {
                case ADD:
                    ps = getAddPrepStatement(obj);
                    break;
                case UPDATE:
                    ps = getUpdatePrepStatement(obj);
                    break;
                case DELETE:
                    ps = getDeletePrepStatement(obj);
                    break;
            }
            result = ps.execute();
        } catch (SQLException e) {
            throw new DaoException("Error in method DaoEntity.getBoolResultQuery() \n" +
                    e.getMessage(), e);
        }
        return result;
    }

    public T castParameterToEntity(Object objectEntity) {
        T objCast = null;
        try {
            objCast = (T) objectEntity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return objCast;
    }

    protected PreparedStatement getAddOrUpdateStatement(Object obj, String sql) throws SQLException {
        T docCast = castParameterToEntity(obj);
        PreparedStatement preparedStatement = jdbcController.getConnection().prepareStatement(sql);
        setValues(preparedStatement, docCast);
        return preparedStatement;
    }

    protected PreparedStatement getWhereIdStatement(Object paramEntityId, String sql) throws SQLException {
        PreparedStatement ps = jdbcController.getConnection().prepareStatement(sql);
        try {
            ps.setLong(1, (long) paramEntityId);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return ps;
    }

    public List<T> getAll() throws DaoException {
        List<T> list = null;
        try {
            ResultSet rs = getAllResultSet();
            list = new ArrayList<>();
            while (rs.next()) {
                list.add(getEntity(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Error in method DaoEntity.getAll() \n" + e.getMessage(), e);
        } catch (JdbcControllerException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public T getById(long id) throws DaoException {
        T entity = null;
        try {
            PreparedStatement ps = getByIdPrepStatement(id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                entity = getEntity(rs);
            }
        } catch (SQLException e) {
            throw new DaoException("Error in method DaoEntity.getById() \n" + e.getMessage(), e);
        }
        return entity;
    }


}
