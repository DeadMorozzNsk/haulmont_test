package com.haulmont.testtask.dao;

import com.haulmont.testtask.dao.database.JdbcController;
import com.haulmont.testtask.dao.exceptions.DaoException;
import com.haulmont.testtask.dao.exceptions.JdbcControllerException;
import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.domain.Priority;
import com.haulmont.testtask.domain.Recipe;

import java.sql.*;

public class DaoRecipe extends DaoEntity<Recipe> {
    protected DaoEntityType type = DaoEntityType.DAO_RECIPE;

    @Override
    protected Recipe getEntity(ResultSet rs) throws SQLException {
        Doctor doctor = null;
        Patient patient = null;
        Priority priority = null;
        try {
            doctor = DaoFactory.getInstance().getDaoDoctor().getById(rs.getLong("DOCTOR_ID"));
            patient = DaoFactory.getInstance().getDaoPatient().getById(rs.getLong("PATIENT_ID"));
            priority = DaoFactory.getInstance().getDaoPriority().getById(rs.getLong("PRIORITY_ID"));
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        return new Recipe( /* same order as declared in class due to Lombok annotation */
                rs.getLong("ID"),
                rs.getString("DESCRIPTION"),
                rs.getLong("PATIENT_ID"),
                rs.getLong("DOCTOR_ID"),
                rs.getDate("CREATION_DATE"),
                rs.getDate("EXPIRATION_DATE"),
                rs.getLong("PRIORITY_ID"),
                patient,
                doctor,
                priority);
    }

    @Override
    protected ResultSet getAllResultSet() throws JdbcControllerException {
        return JdbcController.getInstance().executeQuery("SELECT * FROM RECIPES");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Object paramEntity) throws SQLException {
        return getAddOrUpdateStatement(paramEntity,
                "INSERT INTO RECIPES (" +
                        "DESCRIPTION, " +
                        "PATIENT_ID, " +
                        "DOCTOR_ID, " +
                        "CREATION_DATE, " +
                        "EXPIRATION_DATE, " +
                        "PRIORITY_ID) VALUES (?, ?, ?, ?, ?, ?)");
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Object paramEntity) throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(paramEntity,
                "UPDATE RECIPES SET " +
                        "DESCRIPTION = ?, " +
                        "PATIENT_ID = ?, " +
                        "DOCTOR_ID = ?, " +
                        "CREATION_DATE = ?, " +
                        "EXPIRATION_DATE = ?, " +
                        "PRIORITY_ID = ? WHERE ID = ?");
        ps.setLong(7, ((Recipe) paramEntity).getId()); /* Class was casted in method getAddOrUpdateStatement */
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId, "DELETE FROM RECIPES WHERE ID = ?");
    }

    @Override
    protected PreparedStatement getByIdPrepStatement(Object paramEntityId) throws SQLException {
        return getWhereIdStatement(paramEntityId, "SELECT * FROM RECIPES WHERE ID = ?");
    }

    @Override
    protected void setValues(PreparedStatement stmt, Recipe entity) throws SQLException {
        stmt.setString(1, entity.getDescription());
        stmt.setLong(2, entity.getPatientId());
        stmt.setLong(3, entity.getDoctorId());
        stmt.setDate(4, new Date(entity.getCreationDate().getTime()));
        stmt.setDate(5, new Date(entity.getExpirationDate().getTime()));
        stmt.setLong(6, entity.getPriorityId());
    }

    @Override
    public boolean deleteAvailable(long id){
        return true;
    }
}
