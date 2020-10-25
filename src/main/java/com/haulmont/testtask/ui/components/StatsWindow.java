package com.haulmont.testtask.ui.components;

import com.vaadin.ui.*;

public class StatsWindow extends Window {
    Button closeButton = new Button("Закрыть");

    public StatsWindow(FormLayout mainFormLayout) {
        setStyleName(Validator.MODAL_WINDOW);
        HorizontalLayout hBox = new HorizontalLayout();
        hBox.addComponent(closeButton);
        closeButton.addClickListener(clickEvent -> close());
        mainFormLayout.addComponent(hBox);
        setResizable(false);
        setModal(true);
        setWidth("450px");
        setHeight("450px");
        setContent(mainFormLayout);
    }
}
