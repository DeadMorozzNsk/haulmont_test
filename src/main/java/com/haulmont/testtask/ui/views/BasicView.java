package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DaoEntityType;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.dao.exceptions.DaoException;
import com.haulmont.testtask.domain.Entity;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.ui.components.Validator;
import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.server.Setter;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

public abstract class BasicView<T extends Entity> extends VerticalLayout implements View {
    protected Grid<T> entityGrid = initGrid();
    protected Button addButton;
    protected Button editButton;
    protected Button deleteButton;
    protected Binder<T> binder;

    protected Logger logger = initLogger();

    protected abstract void buildView();

    protected abstract void fillGridColumns();

    protected abstract Logger initLogger();

    protected abstract Grid<T> initGrid();

    protected abstract void setAddButtonListener();

    protected abstract void setEditButtonListener();

    protected abstract DaoEntityType getDaoEntityType();

    protected void setDeleteButtonListener() {
        deleteButton.addClickListener(clickEvent -> {
            if (!entityGrid.asSingleSelect().isEmpty()) {
                try {
                    DaoFactory.getInstance().getDaoByType(getDaoEntityType()).delete(entityGrid.asSingleSelect().getValue().getId());
                    refreshGrid();
                } catch (DaoException e) {
                    if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                        Notification notification = new Notification("Удаление объекта невозможно! " +
                                "Имеются рецепты, в которых он зайдествован.");
                        notification.setDelayMsec(2000);
                        notification.show(Page.getCurrent());
                    } else {
                        logger.severe(e.getMessage());
                    }
                }
            }
        });
    }

    protected void refreshGrid() {
        try {
            List<? extends Entity> entities = DaoFactory.getInstance().getDaoByType(getDaoEntityType()).getAll();
            entityGrid.setItems((List<T>) entities);
        } catch (DaoException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    ;

    public abstract boolean addToDB(T entity);

    public abstract boolean updateInDB(T entity);

    public abstract T getNewEntity();

    public abstract void setEntityFieldsValues(T entity);

    public abstract void setFieldsValues(T entity);

    protected abstract FormLayout getEditFormView();

    public BasicView() {
        binder = new Binder<>();
    }

    public Grid<T> getEntityGrid() {
        return entityGrid;
    }

    protected void setEntityGridListener() {
        entityGrid.addSelectionListener(valueChangeEvent -> {
            setEditDeleteButtonsEnabled(!entityGrid.asSingleSelect().isEmpty());
        });
    }

    protected Layout getButtonsLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        addButton = new Button("Добавить");
        addButton.setIcon(VaadinIcons.PLUS);
        editButton = new Button("Изменить");
        editButton.setIcon(VaadinIcons.EDIT);
        deleteButton = new Button("Удалить");
        deleteButton.setIcon(VaadinIcons.TRASH);
        setEditDeleteButtonsEnabled(false);
        layout.addComponents(addButton, editButton, deleteButton);
        return layout;
    }

    protected void setButtonsListeners() {
        try {
            setEntityGridListener();
            setAddButtonListener();
            setEditButtonListener();
            setDeleteButtonListener();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    protected void setEditDeleteButtonsEnabled(boolean isEnabled) {
        editButton.setEnabled(isEnabled);
        deleteButton.setEnabled(isEnabled);
    }

    public TextField getNewTextField(String caption, ValueProvider<T, String> getter, Setter<T, String> setter) {
        TextField resultField = getNewInputField(caption, getter, setter);
        bindNameField(resultField, getter, setter, Validator::nameIsValid);
        return resultField;
    }

    public TextField getNewNumberField(String caption, ValueProvider<T, String> getter, Setter<T, String> setter) {
        TextField resultField = getNewInputField(caption, getter, setter);
        bindNameField(resultField, getter, setter, Validator::phoneIsValid);
        return resultField;
    }

    public TextField getNewInputField(String caption, ValueProvider<T, String> getter, Setter<T, String> setter) {
        TextField resultField = new TextField(caption);
        resultField.setMaxLength(32);
        resultField.setWidth("100%");
        resultField.setRequiredIndicatorVisible(true);
        return resultField;
    }

    protected void bindNameField(TextField field, ValueProvider<T, String> getter,
                                 Setter<T, String> setter, SerializablePredicate<? super String> validator) {
        binder.forField(field)
                .withValidator(validator, "Проверьте правильность заполнения полей!")
                .asRequired()
                .bind(getter, setter);
    }
}
