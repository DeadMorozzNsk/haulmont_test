package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.domain.Recipe;

public class StatsWindow extends AbstractWindow<Recipe> {

    public StatsWindow() {

    }

    @Override
    protected void setOkButtonListener() {
        this.acceptButton.addClickListener(clickEvent -> {
            close();
        });
    }

    @Override
    protected void setFieldsValues(Recipe entity) {

    }
}
