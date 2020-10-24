package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.domain.Entity;
import com.haulmont.testtask.ui.views.BasicView;
import com.vaadin.ui.*;

public class EntityEditWindow<T extends Entity> extends Window {
    VerticalLayout mainFormLayout = new VerticalLayout();
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    private Button okButton = new Button("ОК");
    private Button cancelButton = new Button("Отменить");
    T entity;


    public EntityEditWindow(ActionType actionType, BasicView<T> basicView, FormLayout formLayout) {
        setStyleName(AbstractWindow.MODAL_WINDOW);
        setResizable(false);
        setModal(true);
        setWidth("450px");
        setHeight("360px");
        formLayout.setMargin(true);
        initEntity(actionType, basicView);
        attachButtonsListeners(basicView, actionType);
        if (actionType == ActionType.EDIT) basicView.setFieldsValues(entity);
        center();
        buttonsLayout.addComponents(okButton, cancelButton);
        formLayout.addComponent(buttonsLayout);
        mainFormLayout.addComponent(formLayout);
        setContent(mainFormLayout);
    }

    public void initEntity(ActionType actionType, BasicView<T> basicView) {
        if (actionType == ActionType.EDIT ) {
            entity = basicView.getEntityGrid().asSingleSelect().getValue();
        } else {
            entity = basicView.getNewEntity();
        }
    }

    public void attachButtonsListeners(BasicView<T> basicView, ActionType actionType) {
        okButton.addClickListener(event -> {
            switch (actionType) {
                case ADD:
                    if (!basicView.addToDB(entity))return;
                    break;
                case EDIT:
                    if (!basicView.updateInDB(entity)) return;
                    break;
            }
            close();
        });

        cancelButton.addClickListener(event -> close());
    }
}
