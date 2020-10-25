package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.backend.domain.Entity;
import com.haulmont.testtask.ui.views.BasicView;
import com.vaadin.ui.*;

public class EntityEditWindow<T extends Entity> extends Window {
    VerticalLayout mainFormLayout = new VerticalLayout();
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    private final Button okButton = new Button("ОК");
    private final Button cancelButton = new Button("Отменить");
    T entity;

    /**
     * конструктор формы редактирования сущности
     * генерирует шаблон формы, в который будут подгружены
     * элементы управления полями UI
     *
     * @param actionType тип действия
     *                   добавление нового/редактирование выбранного
     * @param basicView  родительский экран
     * @param formLayout раскладка элементов формы
     */
    public EntityEditWindow(ActionType actionType, BasicView<T> basicView, Layout formLayout) {
        setStyleName(Validator.MODAL_WINDOW);
        setResizable(false);
        setModal(true);
        setClosable(false);
        setWidth("600px");
        initEntity(actionType, basicView);
        attachButtonsListeners(basicView, actionType);
        if (actionType == ActionType.EDIT) basicView.setFieldsValues(entity);
        center();
        buttonsLayout.addComponents(okButton, cancelButton);
        formLayout.addComponent(buttonsLayout);
        mainFormLayout.addComponent(formLayout);
        setContent(mainFormLayout);
    }

    /**
     * получение объекта сущности в зависимости от типа операции
     *
     * @param actionType тип операции:
     *                   добавление/редактирование
     * @param basicView  Экран-родитель
     */
    public void initEntity(ActionType actionType, BasicView<T> basicView) {
        if (actionType == ActionType.EDIT) {
            entity = basicView.getEntityGrid().asSingleSelect().getValue();
        } else {
            entity = basicView.getNewEntity();
        }
    }

    /**
     * назначение обработчиков стандартных кнопок формы
     *
     * @param basicView  экран-родитель
     * @param actionType тип операции:
     *                   добавление/редактирование
     */
    public void attachButtonsListeners(BasicView<T> basicView, ActionType actionType) {
        okButton.addClickListener(event -> {
            switch (actionType) {
                case ADD:
                    if (!basicView.addToDB(entity)) return;
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
