package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.dao.DaoPatient;
import com.haulmont.testtask.domain.Patient;
import com.vaadin.ui.Grid;

import java.util.List;
import java.util.logging.Logger;

public class PatientEditWindow extends PersonEditWindow<Patient> {
    private final Grid<Patient> patientGrid;
    private Patient patient;
    private final Logger logger = Logger.getLogger(PatientEditWindow.class.getName());

    public PatientEditWindow(ActionType action, Grid<Patient> grid) {
        super();
        this.patientGrid = grid;
        personField.setCaption("Телефон");
        if (action == ActionType.EDIT) {
            patient = patientGrid.asSingleSelect().getValue();
            setFieldsValues(patient);
        }
        setOkButtonListener(action);
    }

    @Override
    protected void setFieldsValues(Patient entity) {
        nameField.setValue(entity.getName());
        surnameField.setValue(entity.getSurname());
        patronymField.setValue(entity.getPatronym());
        personField.setValue(entity.getPhoneNumber());
    }

    protected void setOkButtonListener(ActionType action) {
        if (action == ActionType.EDIT) {
            setCaption("Редактирование пациента");
            if (!patientGrid.asSingleSelect().isEmpty()) {
                try {
                    binder.setBean(patient);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавление пациента");
            surnameField.focus();
        }

        acceptButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    Patient newPatient = new Patient();
                    newPatient.setSurname(surnameField.getValue());
                    newPatient.setName(nameField.getValue());
                    newPatient.setPatronym(patronymField.getValue());
                    newPatient.setPhoneNumber(personField.getValue());
                    DaoPatient patientDao = DaoFactory.getInstance().getDaoPatient();
                    if (action == ActionType.EDIT) {
                        patientDao.update(patient);
                    } else {
                        patientDao.add(newPatient);
                    }
                    List<Patient> patients = DaoFactory.getInstance().getDaoPatient().getAll();
                    patientGrid.setItems(patients);
                } catch (DaoException e) {
                    logger.severe(e.getMessage());
                }
                close();
            }
        });
    }
}
