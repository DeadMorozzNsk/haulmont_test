package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.domain.Patient;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;

import java.util.List;
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
        setMargin(true);
        setSpacing(true);
        setSizeFull();
        addComponents(entityGrid, buttons);
        setExpandRatio(entityGrid, 1f);
        setButtonsListeners();
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
    protected void setAddButtonListener() {
//        addButton.addClickListener(clickEvent -> getUI().addWindow(new PatientWindow(entityGrid, false)));
    }

    @Override
    protected void setEditButtonListener() {
//        editButton.addClickListener(clickEvent -> getUI().addWindow(new PatientWindow(entityGrid, true)));
    }

    @Override
    protected void setDeleteButtonListener() {
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
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshGrid();
    }

    private void refreshGrid() {
        try {
            List<Patient> entities = DaoFactory.getInstance().getDaoPatient().getAll();
            entityGrid.setItems(entities);
        } catch (DaoException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
