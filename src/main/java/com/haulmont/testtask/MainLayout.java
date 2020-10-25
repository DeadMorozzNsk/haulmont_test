package com.haulmont.testtask;

import com.haulmont.testtask.ui.components.Validator;
import com.haulmont.testtask.ui.views.DoctorsView;
import com.haulmont.testtask.ui.views.PatientsView;
import com.haulmont.testtask.ui.views.RecipesView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.*;

public class MainLayout extends VerticalLayout {

    private final Navigator navigator;

    public MainLayout() {
        this.setSizeFull();
        this.setMargin(false);
        this.setSpacing(false);
        Layout pageHeader = createHeaderNavigationLayout();
        Layout workArea = createWorkingAreaLayout();
        this.addComponents(pageHeader, workArea);
        this.setExpandRatio(workArea, 1f);

        ViewDisplay viewDisplay = new Navigator.ComponentContainerViewDisplay(workArea);
        navigator = new Navigator(UI.getCurrent(), viewDisplay);
        navigator.addView(DoctorsView.NAME, new DoctorsView());
        navigator.addView(PatientsView.NAME, new PatientsView());
        navigator.addView(RecipesView.NAME, new RecipesView());

        pageHeader.setStyleName(Validator.HEADER_LAYOUT);
        this.setStyleName(Validator.VIEW_LAYOUT);
        workArea.setStyleName(Validator.VIEW_LAYOUT);

        navigator.navigateTo(PatientsView.NAME); /* начинаем со страницы пациентов*/
    }

    private Layout createHeaderNavigationLayout() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("48px");
        headerLayout.setWidth("100%");
        headerLayout.setMargin(false);
        headerLayout.setSpacing(true);

        Button patientButton = createButton("Пациенты", PatientsView.NAME, VaadinIcons.USERS);
        Button doctorButton = createButton("Докторы", DoctorsView.NAME, VaadinIcons.DOCTOR);
        Button recipeButton = createButton("Рецепты", RecipesView.NAME, VaadinIcons.CLIPBOARD_TEXT);
        Label header = new Label("Поликлиника \"Прощай, здоровье!\"");
        header.setWidth(null);

        headerLayout.addComponents(patientButton, doctorButton, recipeButton, header);
        headerLayout.setComponentAlignment(header, Alignment.MIDDLE_RIGHT);
        headerLayout.setExpandRatio(header, 1f);
        return headerLayout;
    }

    private Layout createWorkingAreaLayout() {
        VerticalLayout wArea = new VerticalLayout();
        wArea.setSizeFull();
        wArea.setMargin(false);
        wArea.setSpacing(true);
        return wArea;
    }

    private Button createButton(String caption, String navigateTo, VaadinIcons icon) {
        Button resultButton = new Button(caption, clickEvent -> navigator.navigateTo(navigateTo));
        if (icon != null) resultButton.setIcon(icon);
        resultButton.setHeight("100%");
        resultButton.addStyleName(Validator.BORDERLESS);
        return resultButton;
    }
}