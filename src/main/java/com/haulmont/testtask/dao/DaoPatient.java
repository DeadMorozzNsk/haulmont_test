package com.haulmont.testtask.dao;

import com.haulmont.testtask.dao.database.JdbcController;
import com.haulmont.testtask.dao.database.JdbcControllerException;
import com.haulmont.testtask.domain.Patient;

import java.sql.*;

public class DaoPatient extends DaoEntity<Patient> {

    @Override
    protected Patient getEntity(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("SURNAME"),
                rs.getString("PATRONYM"),
                rs.getString("PHONE_NUMBER"));
    }

    @Override
    protected ResultSet getAllResultSet() throws JdbcControllerException {
        return JdbcController.getInstance().executeQuery("SELECT * FROM PATIENTS");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Object paramEntity) throws SQLException {
        return getAddOrUpdateStatement(paramEntity,
                "INSERT INTO PATIENTS (NAME, SURNAME, PATRONYM, PHONE_NUMBER) VALUES (?, ?, ?, ?)");
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(paramEntity,
                "UPDATE PATIENTS SET NAME = ?, SURNAME = ?, PATRONYM = ?, PHONE_NUMBER = ? WHERE ID = ?");
        ps.setLong(5, ((Patient) paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId, "DELETE FROM PATIENTS WHERE ID = ?");
    }

    @Override
    protected PreparedStatement getByIdPrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId,
                "SELECT * FROM PATIENTS WHERE ID = ?");
    }

    @Override
    protected void setValues(PreparedStatement stmt, Patient entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getSurname());
        stmt.setString(3, entity.getPatronym());
        stmt.setString(4, entity.getPhoneNumber());
    }

    @Override
    protected boolean deleteAvailable(long id) throws SQLException {
        PreparedStatement ps = getWhereIdStatement(1,
                "SELECT * FROM RECIPES WHERE PATIENT_ID = ?");
        ResultSet rs = ps.executeQuery();
        return !rs.next();
    }

}
