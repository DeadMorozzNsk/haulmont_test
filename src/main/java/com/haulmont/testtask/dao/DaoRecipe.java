package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Recipe;

import java.sql.*;

public class DaoRecipe extends DaoEntity<Recipe> {

    @Override
    protected Recipe getEntity(ResultSet rs) throws DaoException {
        try {
            return new Recipe( /* same order as declared in class due to Lombok annotation */
                    rs.getLong("ID"),
                    rs.getString("DESCRIPTION"),
                    rs.getLong("PATIENT_ID"),
                    rs.getLong("DOCTOR_ID"),
                    rs.getDate("CREATION_DATE"),
                    rs.getDate("EXPIRATION_DATE"),
                    rs.getLong("PRIORITY_ID"));
        } catch (SQLException e) {
            throw new DaoException("Could not create \"Patient\" entity", e);
        }
    }

    @Override
    protected ResultSet getAllResultSet(Statement statement) throws SQLException {
        return statement.executeQuery("SELECT * FROM RECIPES");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "INSERT INTO RECIPES (" +
                        "DESCRIPTION, " +
                        "PATIENT_ID, " +
                        "DOCTOR_ID, " +
                        "CREATION_DATE, " +
                        "EXPIRATION_DATE, " +
                        "PRIORITY_ID) VALUES (?, ?, ?, ?, ?, ?)");
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Connection connection, Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(connection,
                paramEntity,
                "UPDATE RECIPES SET " +
                        "DESCRIPTION = ?, " +
                        "PATIENT_ID = ?, " +
                        "DOCTOR_ID = ?, " +
                        "CREATION_DATE = ?, " +
                        "EXPIRATION_DATE = ?, " +
                        "PRIORITY_ID = ?, WHERE ID = ?");
        ps.setLong(7, ((Doctor) paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Connection connection, Object paramEntityId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM RECIPES WHERE ID = ?");
        preparedStatement.setLong(1, (Long) paramEntityId);
        return preparedStatement;
    }

    @Override
    protected void setValues(PreparedStatement stmt, Recipe entity) throws SQLException {
        stmt.setString(1, entity.getDescription());
        stmt.setLong(2, entity.getPatientId());
        stmt.setLong(3, entity.getDoctorId());
        stmt.setDate(4, (Date) entity.getCreationDate());
        stmt.setDate(5, (Date) entity.getExpirationDate());
        stmt.setLong(6, entity.getPriorityId());
    }
}
