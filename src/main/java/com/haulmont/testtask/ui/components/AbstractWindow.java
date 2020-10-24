package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.domain.Entity;
import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.server.Setter;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;


public abstract class AbstractWindow<T extends Entity> extends Window {
    public static final String MODAL_WINDOW = "modal-window";
    public static final String VIEW_LAYOUT = "view-layout";
    public static final String HEADER_LAYOUT = "header-layout";
    public static final String BORDERLESS = "borderless";

    protected Button acceptButton;
    protected Button declineButton = new Button("Отмена");
    protected Binder<T> binder = new Binder<>();

    public AbstractWindow() {
        setDeclineButtonListener();
    }

    protected abstract void setOkButtonListener();

    protected abstract void setFieldsValues(T entity);

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

    public static boolean nameIsValid(String text) {
        String regEx = "^[а-яА-ЯёЁa-zA-Z]{0,30}$";
        return text.matches(regEx);
    }

    public static boolean phoneIsValid(String phoneNumber) {
        String regEx = "^((8|\\+\\d{1})\\d{10})$";
        return phoneNumber.matches(regEx);
    }


    protected void setDeclineButtonListener() {
        declineButton.addClickListener(event -> close());
    }

}
