package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.domain.Recipe;
import com.vaadin.ui.Grid;

import java.util.logging.Logger;

public class RecipesView extends BasicView<Recipe> {
    public static final String NAME = "recipes";

    @Override
    protected void buildView() {

    }

    @Override
    protected void fillGridColumns() {

    }

    @Override
    protected Logger initLogger() {
        return null;
    }

    @Override
    protected Grid<Recipe> initGrid() {
        return new Grid<>(Recipe.class);
    }

    @Override
    protected void setButtonsListeners() {

    }

    @Override
    protected String getClassName() {
        return Recipe.class.getName();
    }
}
