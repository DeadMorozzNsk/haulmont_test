package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.domain.Entity;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

import java.util.logging.Logger;

public abstract class BasicView<T extends Entity> extends VerticalLayout implements View {
    protected Grid<T> entityGrid = initGrid();

    protected Button addButton;
    protected Button editButton;
    protected Button deleteButton;

    protected Logger logger = initLogger();

    protected abstract void buildView();

    protected abstract void fillGridColumns();

    protected abstract Logger initLogger();

    protected abstract Grid<T> initGrid();

    protected abstract void setAddButtonListener();

    protected abstract void setEditButtonListener();

    protected abstract void setDeleteButtonListener();

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

}
