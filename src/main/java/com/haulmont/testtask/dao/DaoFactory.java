package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Entity;

public class DaoFactory {
    private static DaoFactory instance = null;

    public static synchronized DaoFactory getInstance() {
        if (instance == null) instance = new DaoFactory();
        return instance;
    }

    public DaoDoctor getDaoDoctor() {
        return new DaoDoctor();
    }

    public DaoPatient getDaoPatient() {
        return new DaoPatient();
    }

    public DaoRecipe getDaoRecipe() {
        return new DaoRecipe();
    }

    public DaoPriority getDaoPriority() {
        return new DaoPriority();
    }

    public DaoEntity getDaoByEntityType(Class<?> entity) {
        try {
            if (Class.forName("com.haulmont.testtask.domain.Doctor").isInstance(entity)) {
                return instance.getDaoDoctor();
            }
            if (Class.forName("com.haulmont.testtask.domain.Patient").isInstance(entity)) {
                return instance.getDaoPatient();
            }
            if (Class.forName("com.haulmont.testtask.domain.Recipe").isInstance(entity)) {
                return instance.getDaoRecipe();
            }
            if (Class.forName("com.haulmont.testtask.domain.Priority").isInstance(entity)) {
                return instance.getDaoPriority();
            }
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
