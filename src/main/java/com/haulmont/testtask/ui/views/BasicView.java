package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.domain.Entity;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.ui.components.AbstractWindow;
import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.server.Setter;
import com.vaadin.ui.*;

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

    protected abstract void setDeleteButtonListener();

    public abstract boolean addToDB(T entity);

    public abstract boolean updateInDB(T entity);

    public abstract T getNewEntity();

    public abstract void setEntityFieldsValues(T entity);

    public abstract void setFieldsValues(T entity);

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
        editButton.setIcon(VaadinIcons.TRASH);
        setEditDeleteButtonsEnabled(false);
        layout.addComponents(addButton, editButton, deleteButton);
        return layout;
    }

    protected void setButtonsListeners(){
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
        bindNameField(resultField, getter, setter, AbstractWindow::nameIsValid);
        return resultField;
    }

    public TextField getNewNumberField(String caption, ValueProvider<T, String> getter, Setter<T, String> setter) {
        TextField resultField = getNewInputField(caption, getter, setter);
        bindNameField(resultField, getter, setter, AbstractWindow::phoneIsValid);
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
