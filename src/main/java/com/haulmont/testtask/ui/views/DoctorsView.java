package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.ui.components.ActionType;
import com.haulmont.testtask.ui.components.DoctorEditWindow;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

public class DoctorsView extends PersonView<Doctor> {
    public static final String NAME = "doctors";
    private Button statButton;

    public DoctorsView() {
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
            getUI().addWindow(new DoctorEditWindow(ActionType.ADD, entityGrid));
            refreshGrid();
        });
    }

    @Override
    protected void setEditButtonListener() {
        editButton.addClickListener(clickEvent -> {
            getUI().addWindow(new DoctorEditWindow(ActionType.EDIT, entityGrid));
            refreshGrid();
        });
    }

    @Override
    protected void setDeleteButtonListener() {
        deleteButton.addClickListener(clickEvent -> {
            if (!entityGrid.asSingleSelect().isEmpty()) {
                try {
                    DaoFactory.getInstance().getDaoDoctor().delete(entityGrid.asSingleSelect().getValue().getId());
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
            List<Doctor> entities = DaoFactory.getInstance().getDaoDoctor().getAll();
            entityGrid.setItems(entities);
        } catch (DaoException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void initStatButton() {
        statButton = new Button("Статистика");
        statButton.addClickListener(event -> {
            //getUI().addWindow();
        });
    }
}