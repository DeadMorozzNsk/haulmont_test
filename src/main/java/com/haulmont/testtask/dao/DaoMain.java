package com.haulmont.testtask.dao;

import java.util.List;

public interface DaoMain<T> {

    T insert(T object) throws DaoException;

    void update(T object) throws DaoException;

    void delete(T object) throws DaoException;

    T getByPrimaryKey(Long key) throws DaoException;

    List<T> getAll() throws DaoException;
}
