package com.haulmont.testtask.ui.components;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Window;

public class StatsWindow extends Window {
    /**
     * конструктор всплывающего модального окна
     * с предустановленной кнопкой закрытия внизу
     * @param mainFormLayout раскладка элементов для отображения на форме
     */
    public StatsWindow(Layout mainFormLayout) {
        setStyleName(Validator.MODAL_WINDOW);
        setResizable(false);
        setModal(true);
        setClosable(false);
        setWidth("700px");
        setHeight(50.0f, Unit.PERCENTAGE);
        Button closeButton = new Button("Закрыть");
        closeButton.addClickListener(clickEvent -> close());
        HorizontalLayout hBox = new HorizontalLayout();
        hBox.addComponent(closeButton);
        mainFormLayout.addComponent(hBox);
        setContent(mainFormLayout);
    }
}
