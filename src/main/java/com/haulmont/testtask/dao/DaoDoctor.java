package com.haulmont.testtask.dao;

import com.haulmont.testtask.dao.database.JdbcController;
import com.haulmont.testtask.dao.database.JdbcControllerException;
import com.haulmont.testtask.domain.Doctor;

import java.sql.*;

public class DaoDoctor extends DaoEntity<Doctor> {
    @Override
    protected Doctor getEntity(ResultSet rs) throws SQLException {
        return new Doctor(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("SURNAME"),
                rs.getString("PATRONYM"),
                rs.getString("SPECIALIZATION"));
    }

    @Override
    protected ResultSet getAllResultSet() throws JdbcControllerException {
        return JdbcController.getInstance().executeQuery("SELECT * FROM DOCTORS");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Object paramEntity) throws SQLException {
        return getAddOrUpdateStatement(paramEntity,
                "INSERT INTO DOCTORS (NAME, SURNAME, PATRONYM, SPECIALIZATION) VALUES (?, ?, ?, ?)");
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(paramEntity,
                "UPDATE DOCTORS SET NAME = ?, SURNAME = ?, PATRONYM = ?, SPECIALIZATION = ? WHERE ID = ?");
        ps.setLong(5, ((Doctor) paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId,"DELETE FROM DOCTORS WHERE ID = ?");
    }

    @Override
    protected PreparedStatement getByIdPrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId, "SELECT * FROM DOCTORS WHERE ID = ?");
    }

    @Override
    protected void setValues(PreparedStatement stmt, Doctor entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getSurname());
        stmt.setString(3, entity.getPatronym());
        stmt.setString(4, entity.getSpecialization());
    }

    @Override
    protected boolean deleteAvailable(long id) throws SQLException {
        PreparedStatement ps = getWhereIdStatement(1,
                "SELECT * FROM RECIPES WHERE DOCTOR_ID = ?");
        ResultSet rs = ps.executeQuery();
        return !rs.next();
    }

}
