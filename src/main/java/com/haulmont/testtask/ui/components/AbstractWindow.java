package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.domain.Entity;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;


public abstract class AbstractWindow<E extends Entity> extends Window {
    public static final String MODAL_WINDOW = "modal-window";
    public static final String VIEW_LAYOUT = "view-layout";
    public static final String HEADER_LAYOUT = "header-layout";
    public static final String BORDERLESS = "borderless";

    protected Button acceptButton;
    protected Button declineButton;

    protected enum ActionType {
        ADD,
        EDIT
    }

    public TextField getNewTextField(String caption, String validatorMessage, Binder binder, boolean asRequired) {
        TextField resultField = new TextField(caption);
        resultField.setMaxLength(32);
        resultField.setWidth("100%");
        resultField.setRequiredIndicatorVisible(true);
        if (asRequired) {
            binder.forField(resultField).asRequired()
                  .withValidator(string -> string != null && !((String)string).isEmpty(), validatorMessage);
        }
        return resultField;
    }

    class StandartInputField<E extends Entity> {
        public Binder<E> binder = new Binder<>();
        TextField result;

        public StandartInputField(String caption) {

        }
    }
}
