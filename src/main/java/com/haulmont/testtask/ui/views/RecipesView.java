package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.exceptions.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.ui.components.ActionType;
import com.haulmont.testtask.ui.components.RecipeEditWindow;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

public class RecipesView extends BasicView<Recipe> {
    public static final String NAME = "recipes";

    public RecipesView() {
        buildView();
    }

    @Override
    protected void buildView() {
        fillGridColumns();
        Layout buttons = getButtonsLayout();
        setMargin(true);
        setSpacing(true);
        setSizeFull();
        addComponents(entityGrid, buttons);
        setExpandRatio(entityGrid, 1f);
        setButtonsListeners();
    }

    @Override
    protected void fillGridColumns() {
        entityGrid.removeAllColumns();
        entityGrid.addColumn(recipe ->
                recipe.getPatient().getSurname() + " " + recipe.getPatient().getName())
                .setCaption("Пациент");
        entityGrid.addColumn(recipe ->
                recipe.getDoctor().getSurname() + " " + recipe.getDoctor().getName())
                .setCaption("Доктор");
        entityGrid.addColumn(Recipe::getCreationDate).setCaption("Дата рецепта");
        entityGrid.addColumn(Recipe::getExpirationDate).setCaption("Действителен до");
        entityGrid.addColumn(recipe -> recipe.getPriority().getName()).setCaption("Приоритет");
        entityGrid.addColumn(Recipe::getDescription).setCaption("Описание");
        entityGrid.setSizeFull();
    }

    @Override
    protected Logger initLogger() {
        return Logger.getLogger(RecipesView.class.getName());
    }

    @Override
    protected Grid<Recipe> initGrid() {
        return new Grid<>(Recipe.class);
    }

    @Override
    protected void setAddButtonListener() {
        addButton.addClickListener(clickEvent -> {
            getUI().addWindow(new RecipeEditWindow(ActionType.ADD, entityGrid));
            refreshGrid();
        });
    }

    @Override
    protected void setEditButtonListener() {
        editButton.addClickListener(clickEvent -> {
            getUI().addWindow(new RecipeEditWindow(ActionType.EDIT, entityGrid));
            refreshGrid();
        });
    }

    @Override
    protected void setDeleteButtonListener() {
        deleteButton.addClickListener(clickEvent -> {
            if (!entityGrid.asSingleSelect().isEmpty()) {
                try {
                    DaoFactory.getInstance().getDaoRecipe().delete(entityGrid.asSingleSelect().getValue().getId());
                    refreshGrid();
                } catch (DaoException e) {
                    if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                        Notification notification = new Notification("Удаление пациента невозможно, " +
                                "так как у него есть активные рецепты");
                        notification.setDelayMsec(2000);
                        notification.show(Page.getCurrent());
                    } else {
                        logger.severe(e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public boolean addToDB(Recipe entity) {
        return false;
    }

    @Override
    public boolean updateInDB(Recipe entity) {
        return false;
    }

    @Override
    public Recipe getNewEntity() {
        return new Recipe();
    }

    @Override
    public void setEntityFieldsValues(Recipe entity) {

    }

    @Override
    public void setFieldsValues(Recipe entity) {

    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshGrid();
    }

    private void refreshGrid() {
        try {
            List<Recipe> entities = DaoFactory.getInstance().getDaoRecipe().getAll();
            entityGrid.setItems(entities);
        } catch (DaoException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
