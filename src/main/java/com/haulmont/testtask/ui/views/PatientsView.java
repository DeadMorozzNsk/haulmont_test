package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DaoEntityType;
import com.haulmont.testtask.dao.exceptions.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.dao.DaoPatient;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.ui.components.ActionType;
import com.haulmont.testtask.ui.components.EntityEditWindow;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

public class PatientsView extends PersonView<Patient> {
    public static final String NAME = "patients";
    private final DaoEntityType daoType = DaoEntityType.DAO_PATIENT;

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
        addButton.addClickListener(clickEvent -> {
            getUI().addWindow(new EntityEditWindow<>(ActionType.ADD, this, getEditFormView()));
        });
    }

    @Override
    protected void setEditButtonListener() {
        editButton.addClickListener(clickEvent -> {
            getUI().addWindow(new EntityEditWindow<>(ActionType.EDIT, this, getEditFormView()));
        });
    }

    @Override
    protected DaoEntityType getDaoEntityType() {
        return daoType;
    }

    @Override
    protected FormLayout getEditFormView() {
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(false);
        formLayout.setSpacing(true);
        surnameField = getNewTextField("Фамилия", Patient::getSurname, Patient::setSurname);
        nameField = getNewTextField("Имя", Patient::getName, Patient::setName);
        patronymField = getNewTextField("Отчество", Patient::getPatronym, Patient::setPatronym);
        personField = getNewNumberField("Телефон", Patient::getPersonField, Patient::setPersonField);
        formLayout.addComponents(surnameField, nameField, patronymField, personField);
        return formLayout;
    }

//    @Override
//    protected void setDeleteButtonListener() {
//        deleteButton.addClickListener(clickEvent -> {
//            if (!entityGrid.asSingleSelect().isEmpty()) {
//                try {
//                    DaoFactory.getInstance().getDaoPatient().delete(entityGrid.asSingleSelect().getValue().getId());
//                    refreshGrid();
//                } catch (DaoException e) {
//                    if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
//                        Notification notification = new Notification("Удаление пациента невозможно, " +
//                                "так как у него есть активные рецепты");
//                        notification.setDelayMsec(2000);
//                        notification.show(Page.getCurrent());
//                    } else {
//                        logger.severe(e.getMessage());
//                    }
//                }
//            }
//        });
//    }

    @Override
    public boolean addToDB(Patient entity) {
        try {
            setEntityFieldsValues(entity);
            DaoPatient patientDao = DaoFactory.getInstance().getDaoPatient();
            patientDao.add(entity);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
            return false;
        }
        refreshGrid();
        return true;
    }

    @Override
    public boolean updateInDB(Patient entity) {
        try {
            setEntityFieldsValues(entity);
            DaoPatient patientDao = DaoFactory.getInstance().getDaoPatient();
            patientDao.update(entity);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
            return false;
        }
        refreshGrid();
        return true;
    }

    @Override
    public void setEntityFieldsValues(Patient entity) {
        entity.setSurname(surnameField.getValue());
        entity.setName(nameField.getValue());
        entity.setPatronym(patronymField.getValue());
        entity.setPhoneNumber(personField.getValue());
    }

    @Override
    public void setFieldsValues(Patient entity) {
        nameField.setValue(entity.getName());
        surnameField.setValue(entity.getSurname());
        patronymField.setValue(entity.getPatronym());
        personField.setValue(entity.getPhoneNumber());
    }

    @Override
    public Patient getNewEntity() {
//        Patient p = new Patient();
//        binder.setBean(p);
        return new Patient();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshGrid();
    }

//    protected void refreshGrid() {
//        try {
//            List<Patient> entities = DaoFactory.getInstance().getDaoPatient().getAll();
//            entityGrid.setItems(entities);
//        } catch (DaoException | NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
}
