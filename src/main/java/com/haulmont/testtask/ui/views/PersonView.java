package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.domain.Person;
import com.vaadin.ui.TextField;

public abstract class PersonView<T extends Person> extends BasicView<T> {
    protected TextField nameField = null;
    protected TextField surnameField = null;
    protected TextField patronymField = null;
    protected TextField personField = null;

//    @Override
//    protected void buildView() {
//        fillGridColumns();
//        Layout buttons = getButtonsLayout();
//        setMargin(true);
//        setSpacing(true);
//        setSizeFull();
//        addComponents(entityGrid, buttons);
//        setExpandRatio(entityGrid, 1f);
//        setButtonsListeners();
//    }
//
}
