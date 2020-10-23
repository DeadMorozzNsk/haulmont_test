package com.haulmont.testtask.dao;

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

}
