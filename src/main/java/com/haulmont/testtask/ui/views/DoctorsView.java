package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.domain.Doctor;
import com.vaadin.ui.Grid;

import java.util.logging.Logger;

public class DoctorsView extends PersonView<Doctor> {
    public static final String NAME = "doctors";

    @Override
    protected Logger initLogger() {
        return null;
    }

    @Override
    protected Grid initGrid() {
        return null;
    }

    @Override
    protected void setButtonsListeners() {

    }
}
