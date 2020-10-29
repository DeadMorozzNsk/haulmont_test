package com.haulmont.testtask.backend.dao;

import com.haulmont.testtask.backend.domain.Entity;

public class DaoFactory {
    private static DaoFactory instance = null;

    private DaoFactory() {
    }

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

    public DaoEntity<? extends Entity> getDaoByType(DaoEntityType type){
        switch (type) {
            case DAO_DOCTOR: return getInstance().getDaoDoctor();
            case DAO_PATIENT: return getInstance().getDaoPatient();
            case DAO_RECIPE: return getInstance().getDaoRecipe();
            case DAO_PRIORITY: return getInstance().getDaoPriority();
        }
        return null;
    }

}
