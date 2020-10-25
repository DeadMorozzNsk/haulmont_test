package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DaoEntityType;
import com.haulmont.testtask.dao.DaoRecipe;
import com.haulmont.testtask.dao.exceptions.DaoException;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.domain.Doctor;
import com.haulmont.testtask.domain.Patient;
import com.haulmont.testtask.domain.Priority;
import com.haulmont.testtask.domain.Recipe;
import com.haulmont.testtask.ui.components.ActionType;
import com.haulmont.testtask.ui.components.EntityEditWindow;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import lombok.Getter;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static com.haulmont.testtask.ui.components.Validator.convertToDateViaInstant;
import static com.haulmont.testtask.ui.components.Validator.convertToLocalDateViaInstant;

@Getter
public class RecipesView extends BasicView<Recipe> {
    public static final String NAME = "recipes";
    private final DaoEntityType daoType = DaoEntityType.DAO_RECIPE;
    TextArea descriptionTextArea;
    DateField creationDateField;
    DateField expirationDateField;
    ComboBox<Patient> patientComboBox;
    ComboBox<Doctor> doctorComboBox;
    ComboBox<Priority> priorityComboBox;

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
        entityGrid.addColumn(Recipe::getDescription).setCaption("Описание");
        entityGrid.addColumn(recipe ->
                recipe.getPatient().getSurname() + " " + recipe.getPatient().getName())
                .setCaption("Пациент");
        entityGrid.addColumn(recipe ->
                recipe.getDoctor().getSurname() + " " + recipe.getDoctor().getName())
                .setCaption("Доктор");
        entityGrid.addColumn(Recipe::getCreationDate).setCaption("Дата рецепта");
        entityGrid.addColumn(Recipe::getExpirationDate).setCaption("Действителен до");
        entityGrid.addColumn(recipe -> recipe.getPriority().getName()).setCaption("Приоритет");
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
            getUI().addWindow(new EntityEditWindow<>(ActionType.ADD, this, getEditFormView()));
            refreshGrid();
        });
    }

    @Override
    protected void setEditButtonListener() {
        editButton.addClickListener(clickEvent -> {
            getUI().addWindow(new EntityEditWindow<>(ActionType.EDIT, this, getEditFormView()));
            refreshGrid();
        });
    }

    @Override
    protected DaoEntityType getDaoEntityType() {
        return daoType;
    }

//    @Override
//    protected void setDeleteButtonListener() {
//        deleteButton.addClickListener(clickEvent -> {
//            if (!entityGrid.asSingleSelect().isEmpty()) {
//                try {
//                    DaoFactory.getInstance().getDaoRecipe().delete(entityGrid.asSingleSelect().getValue().getId());
//                    refreshGrid();
//                } catch (DaoException e) {
//                    logger.severe(e.getMessage());
//                }
//            }
//        });
//    }

    @Override
    public boolean addToDB(Recipe entity) {
        try {
            setEntityFieldsValues(entity);
            DaoRecipe daoRecipe = DaoFactory.getInstance().getDaoRecipe();
            daoRecipe.add(entity);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
            return false;
        }
        refreshGrid();
        return true;
    }

    @Override
    public boolean updateInDB(Recipe entity) {
        try {
            setEntityFieldsValues(entity);
            DaoRecipe daoRecipe = DaoFactory.getInstance().getDaoRecipe();
            daoRecipe.update(entity);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
            return false;
        }
        refreshGrid();
        return true;
    }

    @Override
    public Recipe getNewEntity() {
        return new Recipe();
    }

    @Override
    public void setEntityFieldsValues(Recipe entity) {
        entity.setDescription(descriptionTextArea.getValue());
        entity.setCreationDate(
                convertToDateViaInstant(creationDateField.getValue()));
//                Date.from(creationDateField.getValue()
//                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        entity.setExpirationDate(
                convertToDateViaInstant(expirationDateField.getValue()));
//                Date.from(expirationDateField.getValue()
//                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        entity.setDoctor(doctorComboBox.getValue());
        entity.setDoctorId(doctorComboBox.getValue().getId());
        entity.setPatient(patientComboBox.getValue());
        entity.setPatientId(patientComboBox.getValue().getId());
        entity.setPriority(priorityComboBox.getValue());
        entity.setPriorityId(priorityComboBox.getValue().getId());
    }

    @Override
    public void setFieldsValues(Recipe entity) {
        descriptionTextArea.setValue(entity.getDescription());
        creationDateField.setValue(convertToLocalDateViaInstant(entity.getCreationDate()));
        expirationDateField.setValue(convertToLocalDateViaInstant(entity.getExpirationDate()));
//                LocalDate.from(entity.getExpirationDate().toInstant())
//                .atStartOfDay(ZoneId.systemDefault()).toLocalDate());
        doctorComboBox.setValue(entity.getDoctor());
        patientComboBox.setValue(entity.getPatient());
        priorityComboBox.setValue(entity.getPriority());
    }


    @Override
    protected FormLayout getEditFormView() {
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(false);
        formLayout.setSpacing(true);
        fillDoctorsComboBox();

        fillPatientsComboBox();
        fillPrioritiesComboBox();
        expirationDateField = getNewDateField("Действителен до");
        binder.forField(expirationDateField).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .withValidator(Objects::nonNull, "Пожалуйста, выберите дату окончания действия.")
                .asRequired()
                .bind(Recipe::getExpirationDate, Recipe::setExpirationDate);

        creationDateField = getNewDateField("Дата создания");
        binder.forField(creationDateField).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .withValidator(Objects::nonNull, "Пожалуйста, выберите дату создания.")
                .asRequired()
                .bind(Recipe::getCreationDate, Recipe::setCreationDate);

        descriptionTextArea = new TextArea("Описание");
        descriptionTextArea.setMaxLength(140);
        descriptionTextArea.setWidth("100%");
        binder.forField(descriptionTextArea)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите описание.")
                .asRequired()
                .bind(Recipe::getDescription, Recipe::setDescription);
        VerticalLayout leftSide = new VerticalLayout();
        VerticalLayout rightSide = new VerticalLayout();
        HorizontalLayout hBox = new HorizontalLayout();
        leftSide.addComponents(patientComboBox, doctorComboBox);
        rightSide.addComponents(priorityComboBox, creationDateField, expirationDateField);
        hBox.addComponents(leftSide, rightSide);
        VerticalLayout vBox = new VerticalLayout();
        vBox.addComponents(hBox, descriptionTextArea);
        formLayout.addComponent(vBox);
        return formLayout;
    }

    private DateField getNewDateField(String caption) {
        DateField result = new DateField(caption);
        result.setDateFormat("dd.MM.yyyy");
        result.setPlaceholder("дд.мм.гггг");
        result.setTextFieldEnabled(false);
        result.setWidth("100%");
        return result;
    }

    private void fillDoctorsComboBox() {
        try {
            if (doctorComboBox == null) {
                doctorComboBox = new ComboBox<>();
            } else {
                doctorComboBox.clear();
            }
            List<Doctor> doctors = DaoFactory.getInstance().getDaoDoctor().getAll();
            doctorComboBox.setItems(doctors);
            doctorComboBox.setCaption("Доктор:");
            doctorComboBox.setItemCaptionGenerator(doctor -> doctor.getSurname() + " " + doctor.getName());
            binder.forField(doctorComboBox)
                    .withValidator(Objects::nonNull, "Пожалуйста, укажите доктора!")
                    .asRequired()
                    .bind(Recipe::getDoctor, Recipe::setDoctor);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillPatientsComboBox() {
        try {
            if (patientComboBox == null) {
                patientComboBox = new ComboBox<>();
            } else {
                patientComboBox.clear();
            }
            List<Patient> patients = DaoFactory.getInstance().getDaoPatient().getAll();
            patientComboBox.setItems(patients);
            patientComboBox.setCaption("Пациент:");
            patientComboBox.setItemCaptionGenerator(patient -> patient.getSurname() + " " + patient.getName());
            binder.forField(patientComboBox)
                    .withValidator(Objects::nonNull, "Пожалуйста, укажите пациента!")
                    .asRequired()
                    .bind(Recipe::getPatient, Recipe::setPatient);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillPrioritiesComboBox() {
        try {
            if (priorityComboBox == null) {
                priorityComboBox = new ComboBox<>();
            } else {
                priorityComboBox.clear();
            }
            List<Priority> priorities = DaoFactory.getInstance().getDaoPriority().getAll();
            priorityComboBox.setItems(priorities);
            priorityComboBox.setCaption("Приоритет:");
            priorityComboBox.setItemCaptionGenerator(Priority::getName);
            binder.forField(priorityComboBox)
                    .withValidator(Objects::nonNull, "Пожалуйста, укажите приоритет!")
                    .asRequired()
                    .bind(Recipe::getPriority, Recipe::setPriority);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
        }
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshGrid();
    }

//    protected void refreshGrid() {
//        try {
//            List<Recipe> entities = DaoFactory.getInstance().getDaoByType(daoType).getAll();
//            entityGrid.setItems(entities);
//        } catch (DaoException | NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
}
