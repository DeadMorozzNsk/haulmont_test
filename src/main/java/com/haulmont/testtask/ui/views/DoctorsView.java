package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DaoDoctor;
import com.haulmont.testtask.dao.DaoEntityType;
import com.haulmont.testtask.dao.exceptions.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.dao.exceptions.JdbcControllerException;
import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.ui.components.ActionType;
import com.haulmont.testtask.ui.components.EntityEditWindow;
import com.haulmont.testtask.ui.components.StatsWindow;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DoctorsView extends PersonView<Doctor> {
    public static final String NAME = "doctors";
    private final DaoEntityType daoType = DaoEntityType.DAO_DOCTOR;
    private Button statButton;

    public DoctorsView() {
        buildView();
    }

    @Override
    protected void buildView() {
        fillGridColumns();
        Layout buttons = getButtonsLayout();
        initStatButton();
        buttons.addComponent(statButton);
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
        entityGrid.addColumn(Doctor::getSurname).setCaption("Фамилия");
        entityGrid.addColumn(Doctor::getName).setCaption("Имя");
        entityGrid.addColumn(Doctor::getPatronym).setCaption("Отчество");
        entityGrid.addColumn(Doctor::getSpecialization).setCaption("Специализация");
        entityGrid.setSizeFull();
    }

    @Override
    protected Logger initLogger() {
        return Logger.getLogger(DoctorsView.class.getName());
    }

    @Override
    protected Grid<Doctor> initGrid() {
        return new Grid<>(Doctor.class);
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
    protected Layout getEditFormView() {
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(false);
        formLayout.setSpacing(true);
        surnameField = getNewTextField("Фамилия", Doctor::getSurname, Doctor::setSurname);
        nameField = getNewTextField("Имя", Doctor::getName, Doctor::setName);
        patronymField = getNewTextField("Отчество", Doctor::getPatronym, Doctor::setPatronym);
        personField = getNewTextField("Специализация", Doctor::getPersonField, Doctor::setPersonField);
        formLayout.addComponents(surnameField, nameField, patronymField, personField);
        return formLayout;
    }

    @Override
    public boolean addToDB(Doctor entity) {
        try {
            if (!setEntityFieldsValues(entity)) return false;
            DaoDoctor daoDoctor = DaoFactory.getInstance().getDaoDoctor();
            daoDoctor.add(entity);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
            return false;
        }
        refreshGrid();
        return true;
    }

    @Override
    public boolean updateInDB(Doctor entity) {
        try {
            if (!setEntityFieldsValues(entity)) return false;
            DaoDoctor daoDoctor = DaoFactory.getInstance().getDaoDoctor();
            daoDoctor.update(entity);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
            return false;
        }
        refreshGrid();
        return true;
    }

    @Override
    public Doctor getNewEntity() {
        return new Doctor();
    }

    @Override
    public boolean setEntityFieldsValues(Doctor entity) {
        if (!isFieldValid("^[а-яА-ЯёЁa-zA-Z]{0,30}$", surnameField,
                nameField, patronymField, personField)) return false;
        entity.setSurname(surnameField.getValue());
        entity.setName(nameField.getValue());
        entity.setPatronym(patronymField.getValue());
        entity.setSpecialization(personField.getValue());
        return true;
    }

    @Override
    public void setFieldsValues(Doctor entity) {
        nameField.setValue(entity.getName());
        surnameField.setValue(entity.getSurname());
        patronymField.setValue(entity.getPatronym());
        personField.setValue(entity.getSpecialization());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshGrid();
    }

    protected void initStatButton() {
        statButton = new Button("Статистика");
        statButton.setIcon(VaadinIcons.LINE_BAR_CHART);
        statButton.addClickListener(event -> {
            getUI().addWindow(new StatsWindow(getStatisticsFormView()));
        });
    }

    private Layout getStatisticsFormView() {
        Layout mainLayout = new VerticalLayout();
        Grid<Doctor> statGrid = new Grid<>();
        try {
            List<Doctor> doctors = DaoFactory.getInstance().getDaoDoctor().getAll();
            Map<Long, Integer> stats = DaoFactory.getInstance().getDaoDoctor().getRecipeStatistics();
            statGrid.removeAllColumns();
            statGrid.setItems(doctors);
            statGrid.addColumn(doctor ->
                    doctor.getSurname() + " " + doctor.getName()).setCaption("Доктор");
            statGrid.addColumn(doctor ->
                    stats.get(doctor.getId())).setCaption("Количество рецептов");
        } catch (JdbcControllerException | DaoException e) {
            e.printStackTrace();
        }
        statGrid.setSizeFull();
        mainLayout.addComponent(statGrid);
        return mainLayout;
    }
}
