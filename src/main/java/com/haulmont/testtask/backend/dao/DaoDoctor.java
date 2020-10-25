package com.haulmont.testtask.backend.dao;

import com.haulmont.testtask.backend.dao.database.JdbcController;
import com.haulmont.testtask.backend.dao.exceptions.JdbcControllerException;
import com.haulmont.testtask.backend.domain.Doctor;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DaoDoctor extends DaoEntity<Doctor> {
    protected DaoEntityType type = DaoEntityType.DAO_DOCTOR;

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
        return JdbcController.getInstance()
                .executeQuery("SELECT * FROM DOCTORS");
    }

    @Override
    protected PreparedStatement getAddPrepStatement(Object paramEntity)
            throws SQLException {
        return getAddOrUpdateStatement(paramEntity,
                "INSERT INTO DOCTORS (NAME, SURNAME, PATRONYM, SPECIALIZATION)"
                        + "VALUES (?, ?, ?, ?)");
    }

    @Override
    protected PreparedStatement getUpdatePrepStatement(Object paramEntity)
            throws SQLException {
        PreparedStatement ps = getAddOrUpdateStatement(paramEntity,
                "UPDATE DOCTORS SET NAME = ?, SURNAME = ?, PATRONYM = ?,"
                        + " SPECIALIZATION = ? WHERE ID = ?");
        ps.setLong(5, ((Doctor) paramEntity).getId());
        return ps;
    }

    @Override
    protected PreparedStatement getDeletePrepStatement(Object paramEntityId)
            throws SQLException {
        return getWhereIdStatement(paramEntityId,
                "DELETE FROM DOCTORS WHERE ID = ?");
    }

    @Override
    protected PreparedStatement getByIdPrepStatement(Object paramEntityId)
            throws SQLException {
        return getWhereIdStatement(paramEntityId,
                "SELECT * FROM DOCTORS WHERE ID = ?");
    }

    @Override
    protected void setValues(PreparedStatement stmt, Doctor entity)
            throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getSurname());
        stmt.setString(3, entity.getPatronym());
        stmt.setString(4, entity.getSpecialization());
    }

    @Override
    public boolean deleteAvailable(long id) throws SQLException {
        PreparedStatement ps = getWhereIdStatement(1,
                "SELECT * FROM RECIPES WHERE DOCTOR_ID = ?");
        ResultSet rs = ps.executeQuery();
        return !rs.next();
    }

    /**
     * Получает данные из БД о статистике виписанных рецептов
     *
     * @return мэппинг между ID доктора и количеством рецептов
     * @throws JdbcControllerException
     */
    public Map<Long, Integer> getRecipeStatistics()
            throws JdbcControllerException {
        Map<Long, Integer> stat;
        try {
            stat = new HashMap<>();
            ResultSet rs = JdbcController.getInstance().executeQuery(
                    "SELECT ID,\n"
                    + "ISNULL(TEMP.QUANTITY, 0)   AS QUANTITY\n"
                    + "FROM DOCTORS\n"
                    + "LEFT JOIN (SELECT DOCTOR_ID,\n"
                    + "COUNT(ID) AS QUANTITY\n"
                    + "FROM RECIPES\n"
                    + "GROUP BY DOCTOR_ID) TEMP on ID = TEMP.DOCTOR_ID");

            while (rs.next()) {
                Long id = rs.getLong("ID");
                Integer quantity = rs.getInt("QUANTITY");
                stat.put(id, quantity);
            }
        } catch (SQLException e) {
            throw new JdbcControllerException("Error receiving statistics", e);
        }
        return stat;
    }
}
