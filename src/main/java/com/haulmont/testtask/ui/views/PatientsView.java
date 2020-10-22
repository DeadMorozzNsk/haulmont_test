package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.domain.Patient;
import com.vaadin.ui.Grid;

import java.util.logging.Logger;

public class PatientsView extends PersonView<Patient> {
    public static final String NAME = "patients";

    @Override
    protected Logger initLogger() {
        return Logger.getLogger(PatientsView.class.getName());
    }

    @Override
    protected Grid initGrid() {
        return new Grid<>(Patient.class);
    }

    @Override
    protected void setButtonsListeners() {

    }
}
