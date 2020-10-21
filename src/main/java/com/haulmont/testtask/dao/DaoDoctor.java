package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Doctor;

import java.sql.*;

public class DaoDoctor extends DaoEntity<Doctor> {
    @Override
    protected Doctor getEntity(ResultSet rs) throws DaoException {
        try {
            return new Doctor(
                    rs.getLong("ID"),
                    rs.getString("NAME"),
                    rs.getString("SURNAME"),
                    rs.getString("PATRONYM"),
                    rs.getString("SPECIALIZATION"));
        } catch (SQLException e) {
            throw new DaoException("Could not create \"Doctor\" entity", e);
        }
    }

    @Override
    protected ResultSet getAllResultSet(Statement statement) throws SQLException {
        return statement.executeQuery("SELECT * FROM DOCTORS");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "INSERT INTO DOCTORS (NAME, SURNAME, PATRONYM, SPECIALIZATION) VALUES (?, ?, ?, ?)");
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "UPDATE DOCTORS SET NAME = ?, SURNAME = ?, PATRONYM = ?, SPECIALIZATION = ? WHERE ID = ?");
        ps.setLong(5, ((Doctor)paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Connection connection, Object paramEntityId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM DOCTORS WHERE ID = ?");
        try {
            preparedStatement.setLong(1, (Long) paramEntityId);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected void setValues(PreparedStatement stmt, Doctor entity) throws SQLException {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getSurname());
            stmt.setString(3, entity.getPatronym());
            stmt.setString(4, entity.getSpecialization());
    }

}
