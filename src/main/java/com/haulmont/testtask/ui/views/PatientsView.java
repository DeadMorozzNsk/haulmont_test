package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.domain.Patient;
import com.vaadin.server.Page;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;

import java.util.logging.Logger;

public class PatientsView extends PersonView<Patient> {
    public static final String NAME = "patients";

    public PatientsView() {
        buildView();
    }

    @Override
    protected void buildView() {
        fillGridColumns();
        Layout buttons = getButtonsLayout();
        addComponents(entityGrid, buttons);
        setExpandRatio(entityGrid, 1f);
    }

    @Override
    protected void fillGridColumns() {
        entityGrid.removeAllColumns();
        entityGrid.addColumn(Patient::getSurname).setCaption("Фамилия");
        entityGrid.addColumn(Patient::getName).setCaption("Имя");
        entityGrid.addColumn(Patient::getPatronym).setCaption("Отчество");
        entityGrid.addColumn(Patient::getPhoneNumber).setCaption("Телефон");
        entityGrid.setSizeFull();
    }

    @Override
    protected Logger initLogger() {
        return Logger.getLogger(PatientsView.class.getName());
    }

    @Override
    protected Grid<Patient> initGrid() {
        return new Grid<>(Patient.class);
    }

    @Override
    protected void setButtonsListeners() {
        try {
            entityGrid.addSelectionListener(valueChangeEvent -> {
                if (!entityGrid.asSingleSelect().isEmpty()) {
                    setEditDeleteButtonsEnabled(true);
                } else {
                    setEditDeleteButtonsEnabled(false);
                }
            });

//            addButton.addClickListener(clickEvent ->
//                    getUI().addWindow(new PatientWindow(entityGrid, false)));
//
//            editButton.addClickListener(clickEvent ->
//                    getUI().addWindow(new PatientWindow(entityGrid, true)));

            deleteButton.addClickListener(clickEvent -> {
                if (!entityGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getDaoPatient().delete(entityGrid.asSingleSelect().getValue().getId());
                        refreshGrid();
                    } catch (DaoException e) {
                        if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                            Notification notification = new Notification("Удаление пациента невозможно, " +
                                    "так как у него есть активные рецепты");
                            notification.setDelayMsec(2000);
                            notification.show(Page.getCurrent());
                        } else {
                            logger.severe(e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    protected String getClassName() {
        return Patient.class.getName();
    }
}
