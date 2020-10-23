package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.domain.Person;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

public abstract class PersonEditWindow<T extends Person> extends AbstractWindow<T> {
    protected TextField nameField = null;
    protected TextField surnameField = null;
    protected TextField patronymField = null;
    protected TextField personField = null;

    public PersonEditWindow() {
        buildWindow();
    }

    @Override
    protected void setOkButtonListener() {
        //acceptButton.addClickListener();
    }

    private void buildWindow() {
        setStyleName(AbstractWindow.MODAL_WINDOW);
        setWidth("450px");
        setHeight("360px");
        setModal(true);
        setResizable(false);
        center();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        FormLayout formLayout = getFormLayout();
        Layout buttonsLayout = getButtonsLayout();
        mainLayout.addComponents(formLayout, buttonsLayout);
        mainLayout.setExpandRatio(formLayout, 1f);
        mainLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        setContent(mainLayout);
    }

    private Layout getButtonsLayout() {
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);
        acceptButton = new Button("ОК");
        acceptButton.setWidth("125px");
        declineButton.setWidth("125px");
        buttonsLayout.addComponents(acceptButton, declineButton);
        return buttonsLayout;
    }

    private FormLayout getFormLayout() {
        FormLayout layout = new FormLayout();
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setSpacing(true);
        surnameField = getNewTextField("Фамилия", T::getSurname, T::setSurname);
        nameField = getNewTextField("Имя", T::getName, T::setName);
        patronymField = getNewTextField("Отчество", T::getPatronym, T::setPatronym);
        personField = getNewTextField("void", T::getPersonField, T::setPersonField);
        binder.forField(personField)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите номер телефона.")
                .withValidator(new RegexpValidator("Введите корректный номер телефона.",
                        "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"));
        layout.addComponents(surnameField, nameField, patronymField, personField);

        return layout;
    }
}
