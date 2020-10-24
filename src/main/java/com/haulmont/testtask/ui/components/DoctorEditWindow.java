package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.dao.DaoDoctor;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.domain.Doctor;
import com.vaadin.ui.Grid;

import java.util.List;
import java.util.logging.Logger;

public class DoctorEditWindow extends PersonEditWindow<Doctor> {
    private final Grid<Doctor> doctorGrid;
    private Doctor doctor;
    private final Logger logger = Logger.getLogger(com.haulmont.testtask.ui.components.PatientEditWindow.class.getName());

    public DoctorEditWindow(ActionType action, Grid<Doctor> grid) {
        super();
        this.doctorGrid = grid;
        personField.setCaption("Специализация");
        doctor = doctorGrid.asSingleSelect().getValue();
        setFieldsValues(doctor);
        setOkButtonListener(action);
    }

    @Override
    protected void setFieldsValues(Doctor entity) {
        nameField.setValue(entity.getName());
        surnameField.setValue(entity.getSurname());
        patronymField.setValue(entity.getPatronym());
        personField.setValue(entity.getSpecialization());
    }

    protected void setOkButtonListener(ActionType action) {
        if (action == ActionType.EDIT) {
            setCaption("Редактирование пациента");
            if (!doctorGrid.asSingleSelect().isEmpty()) {
                try {
//                    doctor = doctorGrid.asSingleSelect().getValue();
                    binder.setBean(doctor);
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
                    Doctor newDoctor = new Doctor();
                    newDoctor.setSurname(surnameField.getValue());
                    newDoctor.setName(nameField.getValue());
                    newDoctor.setPatronym(patronymField.getValue());
                    newDoctor.setSpecialization(personField.getValue());
                    DaoDoctor daoDoctor = DaoFactory.getInstance().getDaoDoctor();
                    if (action == ActionType.EDIT) {
                        daoDoctor.update(doctor);
                    } else {
                        daoDoctor.add(newDoctor);
                    }
                    List<Doctor> patients = DaoFactory.getInstance().getDaoDoctor().getAll();
                    doctorGrid.setItems(patients);
                } catch (DaoException e) {
                    logger.severe(e.getMessage());
                }
                close();
            }
        });
    }
}
