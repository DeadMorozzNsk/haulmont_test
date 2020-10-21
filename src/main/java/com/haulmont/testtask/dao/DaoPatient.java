package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Patient;

import java.sql.*;

public class DaoPatient extends DaoEntity<Patient> {

    @Override
    protected Patient getEntity(ResultSet rs) throws DaoException {
        try {
            return new Patient(
                    rs.getLong("ID"),
                    rs.getString("NAME"),
                    rs.getString("SURNAME"),
                    rs.getString("PATRONYM"),
                    rs.getString("PHONE_NUMBER"));
        } catch (SQLException e) {
            throw new DaoException("Could not create \"Patient\" entity", e);
        }
    }

    @Override
    protected ResultSet getAllResultSet(Statement statement) throws SQLException {
        return statement.executeQuery("SELECT * FROM PATIENTS");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "INSERT INTO PATIENTS (NAME, SURNAME, PATRONYM, PHONE_NUMBER) VALUES (?, ?, ?, ?)");
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "UPDATE PATIENTS SET NAME = ?, SURNAME = ?, PATRONYM = ?, PHONE_NUMBER = ? WHERE ID = ?");
        ps.setLong(5, ((Patient) paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Connection connection, Object paramEntityId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM PATIENTS WHERE ID = ?");
        preparedStatement.setLong(1, (Long) paramEntityId);
        return preparedStatement;
    }

    @Override
    protected void setValues(PreparedStatement stmt, Patient entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getSurname());
        stmt.setString(3, entity.getPatronym());
        stmt.setString(4, entity.getPhoneNumber());
    }

}
