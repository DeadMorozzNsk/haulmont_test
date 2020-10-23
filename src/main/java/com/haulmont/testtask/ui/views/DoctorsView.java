package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.domain.Doctor;
import com.vaadin.ui.Grid;

import java.util.logging.Logger;

public class DoctorsView extends PersonView<Doctor> {
    public static final String NAME = "doctors";

    @Override
    protected void buildView() {

    }

    @Override
    protected void fillGridColumns() {

    }

    @Override
    protected Logger initLogger() {
        return null;
    }

    @Override
    protected Grid<Doctor> initGrid() {
        return new Grid<>(Doctor.class);
    }

    @Override
    protected void setButtonsListeners() {

    }

    @Override
    protected String getClassName() {
        return Doctor.class.getName();
    }
}
