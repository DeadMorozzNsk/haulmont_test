package com.haulmont.testtask.backend.dao;

import com.haulmont.testtask.backend.dao.database.JdbcDao;
import com.haulmont.testtask.backend.dao.exceptions.DaoException;
import com.haulmont.testtask.backend.dao.exceptions.JdbcControllerException;
import com.haulmont.testtask.backend.domain.Entity;
import com.haulmont.testtask.backend.dao.database.JdbcController;
import com.haulmont.testtask.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoEntity<T extends Entity> {
    private static JdbcDao jdbcController = JdbcController.getInstance();
    protected DaoEntityType type;

    private enum QueryType {
        ADD,
        UPDATE,
        DELETE
    }

    /**
     * Извлечь сущность из набора записей
     *
     * @param rs набор записей
     * @return объект сущности
     * @throws SQLException
     */
    protected abstract T getEntity(ResultSet rs) throws SQLException;

    /**
     * Подготовка выражения для формирования запроса
     * получения всех сущностей из БД
     *
     * @return набор записей всех сущностей
     * @throws SQLException
     */
    protected abstract ResultSet getAllResultSet() throws JdbcControllerException;

    /**
     * Подготовка выражения для формирования запроса
     * обновления сущности в БД
     *
     * @param paramEntity объект сущности
     * @return выражение для отправки запроса
     * @throws SQLException
     */
    protected abstract PreparedStatement getUpdatePrepStatement(Object paramEntity) throws SQLException;

    /**
     * Подготовка выражения для формирования запроса
     * записи сущности в БД
     *
     * @param paramEntity объект сущности
     * @return выражение для отправки запроса
     * @throws SQLException
     */
    protected abstract PreparedStatement getAddPrepStatement(Object paramEntity) throws SQLException;

    /**
     * Подготовка выражения для формирования запроса
     * удаления сущности за основании id
     *
     * @param paramEntityId id сущности
     * @return выражение для отправки запроса
     * @throws SQLException
     */
    protected abstract PreparedStatement getDeletePrepStatement(Object paramEntityId) throws SQLException;

    /**
     * Подготовка выражения для формирования запроса
     * получения сущности за основании id
     *
     * @param paramEntityId id сущности
     * @return выражение для отправки запроса
     * @throws SQLException
     */
    protected abstract PreparedStatement getByIdPrepStatement(Object paramEntityId) throws SQLException;

    /**
     * установка параметров запроса
     *
     * @param stmt   PreparedStatement для установки параметров
     * @param entity сущность БД
     * @throws SQLException
     */
    protected abstract void setValues(PreparedStatement stmt, T entity) throws SQLException;

    /**
     * Проверка доступности операции удаления
     *
     * @param id идентификатор сущности
     * @return результат выполнения
     * @throws SQLException
     */
    public abstract boolean deleteAvailable(long id) throws SQLException;

    /**
     * Запись сущности в БД
     *
     * @param entity объект сущности для записи
     * @return результат выполнение
     * @throws DaoException
     */
    public boolean add(T entity) throws DaoException {
        return getBoolResultQuery(entity, QueryType.ADD);
    }

    /**
     * Обновление записи сущности в БД
     *
     * @param entity объект сущности для обновления
     * @return результат выполнения запроса
     * @throws DaoException
     */
    public boolean update(T entity) throws DaoException {
        return getBoolResultQuery(entity, QueryType.UPDATE);
    }

    /**
     * Удаляет сущность из БД
     *
     * @param id идентификатор сущности для удаления
     * @return результат выполнения
     * @throws DaoException
     */
    public boolean delete(Long id) throws DaoException {
        /*if (!deleteAvailable(id)) return false*/
        return getBoolResultQuery(id, QueryType.DELETE);
    }

    protected boolean getBoolResultQuery(Object obj, QueryType queryType) throws DaoException {
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

    /**
     * получение списка всех сущностей из БД
     *
     * @return Список сущностей
     * @throws DaoException
     */
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

    /**
     * Получает сущность по ее идентификатору
     *
     * @param id идентификатор сущности
     * @return объект сущности
     * @throws DaoException
     */
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
