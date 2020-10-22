package com.haulmont.testtask.ui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

import java.util.logging.Logger;

public abstract class BasicView<E> extends VerticalLayout implements View {
    protected Grid<E> entityGrid = initGrid();

    protected Button addButton;
    protected Button editButton;
    protected Button deleteButton;

    protected Logger logger = initLogger();

    protected abstract Logger initLogger();

    protected abstract Grid<E> initGrid();

    protected abstract void setButtonsListeners();

    protected void buildView(){

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
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        layout.addComponents(addButton, editButton, deleteButton);
        return layout;
    }


}
