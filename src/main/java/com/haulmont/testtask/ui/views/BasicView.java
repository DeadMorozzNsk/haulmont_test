package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.backend.dao.DaoEntityType;
import com.haulmont.testtask.backend.dao.DaoFactory;
import com.haulmont.testtask.backend.dao.exceptions.DaoException;
import com.haulmont.testtask.backend.domain.Entity;
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

public abstract class BasicView<T extends Entity>
        extends VerticalLayout implements View {
    protected Grid<T> entityGrid = initGrid();
    protected Button addButton;
    protected Button editButton;
    protected Button deleteButton;
    protected Binder<T> binder;

    protected Logger logger = initLogger();

    /**
     * главная процедура генерации страницы
     */
    protected abstract void buildView();

    /**
     * формирование столбцов таблиц сущностей
     */
    protected abstract void fillGridColumns();

    /**
     * инициализация логгера для страницы
     *
     * @return логгер
     */
    protected abstract Logger initLogger();

    /**
     * инициализация таблицы сущностей
     *
     * @return объект Grid в соответствии с классом
     * сущностей
     */
    protected abstract Grid<T> initGrid();

    /**
     * обработчик кнопки добавления
     */
    protected abstract void setAddButtonListener();

    /**
     * обработчик кнопки редактирования
     */
    protected abstract void setEditButtonListener();

    /**
     * обработчик кнопки получение типа сущности для передачи
     * в фабрику DAO
     *
     * @return <type>DaoEntityType</type> - тип сущности
     */
    protected abstract DaoEntityType getDaoEntityType();

    /**
     * запись новой сущности в БД
     *
     * @param entity сущность для записи
     * @return результат выполнения true/false
     */
    public abstract boolean addToDB(T entity);

    /**
     * обновление записи сущности в БД
     *
     * @param entity сущность для обносления
     * @return результат выполнения true/false
     */
    public abstract boolean updateInDB(T entity);

    /**
     * получает новый объект сущности
     *
     * @return новая сущность
     */
    public abstract T getNewEntity();

    /**
     * установка значений полей объекта сущности в поля UI
     *
     * @param entity сущность
     * @return результат выполнения
     */
    public abstract boolean setEntityFieldsValues(T entity);

    /**
     * запись значений полей формы UI в поля объекта сущности
     *
     * @param entity
     */
    public abstract void setFieldsValues(T entity);

    /**
     * получение раскладки элементов UI для формы
     * добавления/редактирования сущности в БД
     *
     * @return раскладка элементов формы
     */
    protected abstract Layout getEditFormView();

    /**
     * конструктор с инициализацией биндера по умолчанию
     */
    public BasicView() {
        binder = new Binder<>();
    }

    public Grid<T> getEntityGrid() {
        return entityGrid;
    }

    protected void setEntityGridListener() {
        entityGrid.addSelectionListener(valueChangeEvent ->
                setEditDeleteButtonsEnabled(!entityGrid.asSingleSelect().isEmpty()));
    }

    /**
     * обновление таблицы сущностей
     */
    protected void refreshGrid() {
        try {
            List<? extends Entity> entities = DaoFactory.getInstance().getDaoByType(getDaoEntityType()).getAll();
            entityGrid.setItems((List<T>) entities);
        } catch (DaoException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * проверка текстовых полей UI на заполнение данными
     * и соответствию регулярному выражению
     *
     * @param regEx  регулярное выражение
     * @param fields текстовые поля
     * @return все ли поля валидны true/false
     */
    protected boolean isFieldValid(String regEx, AbstractField... fields) {
        boolean result = true;
        for (AbstractField field :
                fields) {
            if (field.getValue() == null || String.valueOf(field.getValue()).length() == 0 ||
                    !((String) field.getValue()).matches(regEx)) {
                sendNotification("Не все обязательные поля заполнены!");
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * удаление сужности из БД по её индексу
     * отлавливает <exception>SQLIntegrityConstraintViolationException</exception>
     * при попытке удалить сущности, являющиеся
     * внешними ключами для других таблиц,
     * выводит соответствующее предупреждение
     */
    protected void setDeleteButtonListener() {
        deleteButton.addClickListener(clickEvent -> {
            if (!entityGrid.asSingleSelect().isEmpty()) {
                try {
                    DaoFactory.getInstance().getDaoByType(getDaoEntityType())
                            .delete(entityGrid.asSingleSelect().getValue().getId());
                    refreshGrid();
                } catch (DaoException e) {
                    if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                        sendNotification("Удаление объекта невозможно! " +
                                "Имеются рецепты, в которых он зайдествован.");
                    } else {
                        logger.severe(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Вывод предупреждения пользователю
     *
     * @param text текст предупрежденич
     */
    public void sendNotification(String text) {
        Notification notification = new Notification(text);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

    /**
     * генерация раскладки кнопок Добавить/Редактировать/Удалить
     *
     * @return раскладка UI элементов
     */
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

    /**
     * Назначение обработчиков кнопкам
     */
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

    /**
     * установка активности кнопок  редактирования и удаления
     * сущностей
     *
     * @param isEnabled сделать активными true/false
     */
    protected void setEditDeleteButtonsEnabled(boolean isEnabled) {
        editButton.setEnabled(isEnabled);
        deleteButton.setEnabled(isEnabled);
    }

    /**
     * создает новое текстовое поле для UI с валидатором
     * текстового поля для имени
     *
     * @param caption заголовок поля
     * @param getter  метод для получения данных
     * @param setter  метод для передачи данных
     * @return текстовой поле UI
     */
    public TextField getNewTextField(String caption, ValueProvider<T, String> getter, Setter<T, String> setter) {
        TextField resultField = getNewInputField(caption);
        bindNameField(resultField, getter, setter, Validator::nameIsValid);
        return resultField;
    }

    /**
     * создает новое текстовое поле для UI с валидатором
     * текстового поля для цифр
     *
     * @param caption заголовок поля
     * @param getter  метод для получения данных
     * @param setter  метод для передачи данных
     * @return текстовой поле UI
     */
    public TextField getNewNumberField(String caption, ValueProvider<T, String> getter, Setter<T, String> setter) {
        TextField resultField = getNewInputField(caption);
        bindNameField(resultField, getter, setter, Validator::phoneIsValid);
        return resultField;
    }

    /**
     * создает новое текстовое поле для UI без валидатора
     *
     * @param caption заголовок поля
     * @return текстовое поле UI
     */
    public TextField getNewInputField(String caption) {
        TextField resultField = new TextField(caption);
        resultField.setMaxLength(32);
        resultField.setWidth("100%");
        resultField.setRequiredIndicatorVisible(true);
        return resultField;
    }

    /**
     * Установка валидаторов на текстовое поле
     * и маркера "необходимо заполнить" для текстового поля
     *
     * @param field     поле для привязки валидации
     * @param getter    метод для получения данных
     * @param setter    метод отдачи данных из поля
     * @param validator метод-валидатор
     */
    protected void bindNameField(TextField field, ValueProvider<T, String> getter,
                                 Setter<T, String> setter, SerializablePredicate<? super String> validator) {
        binder.forField(field)
                .withValidator(validator, "Проверьте правильность заполнения полей!")
                .withValidator(string -> string != null && !string.isEmpty(), "Поля не должны быть пустыми!")
                .asRequired()
                .bind(getter, setter);
    }
}