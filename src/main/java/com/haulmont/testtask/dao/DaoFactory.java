package com.haulmont.testtask.dao;

import com.haulmont.testtask.domain.*;

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

    public static <T extends DaoEntity> DaoEntity<? extends Entity> getDaoByEntityType(String className) {
        if (Doctor.class.getName().equals(className)) {
            return instance.getDaoDoctor();
        }
        if (Patient.class.getName().equals(className)) {
            return instance.getDaoPatient();
        }
        if (Recipe.class.getName().equals(className)) {
            return instance.getDaoRecipe();
        }
        if (Priority.class.getName().equals(className)) {
            return instance.getDaoPriority();
        }
        return null;
    }
}
