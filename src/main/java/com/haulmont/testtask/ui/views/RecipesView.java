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
import com.haulmont.testtask.ui.components.Validator;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import lombok.Getter;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static com.haulmont.testtask.ui.components.Validator.convertToLocalDateViaInstant;

@Getter
public class RecipesView extends BasicView<Recipe> {
    public static final String NAME = "recipes";
    private final DaoEntityType daoType = DaoEntityType.DAO_RECIPE;
    private TextArea descriptionTextArea;
    private DateField creationDateField;
    private DateField expirationDateField;
    private ComboBox<Patient> patientComboBox;
    private ComboBox<Doctor> doctorComboBox;
    private ComboBox<Priority> priorityComboBox;

    private TextField descriptionFilterField;
    private TextField priorityFilterField;
    private TextField patientFilterField;

    public RecipesView() {
        buildView();
    }

    @Override
    protected void buildView() {
        Panel filterPanel = new Panel("Фильтр");
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setMargin(true);
        filterLayout.setSpacing(true);
        descriptionFilterField = new TextField();
        descriptionFilterField.setCaption("Описание:");
        descriptionFilterField.addValueChangeListener(this::onFilterChangeEvent);

        priorityFilterField = new TextField();
        priorityFilterField.setCaption("Приоритет:");
        priorityFilterField.addValueChangeListener(this::onFilterChangeEvent);

        patientFilterField = new TextField();
        patientFilterField.setCaption("Пациент:");
        patientFilterField.addValueChangeListener(this::onFilterChangeEvent);
        filterLayout.addComponents(descriptionFilterField, priorityFilterField, patientFilterField);
        filterPanel.setContent(filterLayout);

        fillGridColumns();
        Layout buttons = getButtonsLayout();
        setMargin(true);
        setSpacing(true);
        setSizeFull();
        addComponents(filterPanel, entityGrid, buttons);
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

    @Override
    public boolean addToDB(Recipe entity) {
        try {
            if (!setEntityFieldsValues(entity)) return false;
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
            if (!setEntityFieldsValues(entity)) return false;
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
    public boolean setEntityFieldsValues(Recipe entity) {
        try {
            if (!isFieldValid("^[а-яА-ЯёЁa-zA-Z]{0,30}$", descriptionFilterField,
                    creationDateField, expirationDateField)) return false;
            entity.setDescription(descriptionTextArea.getValue());
            entity.setCreationDate(
                    Validator.convertToDateViaInstant(creationDateField.getValue()));
            entity.setExpirationDate(
                    Validator.convertToDateViaInstant(expirationDateField.getValue()));
            entity.setDoctor(doctorComboBox.getValue());
            entity.setDoctorId(doctorComboBox.getValue().getId());
            entity.setPatient(patientComboBox.getValue());
            entity.setPatientId(patientComboBox.getValue().getId());
            entity.setPriority(priorityComboBox.getValue());
            entity.setPriorityId(priorityComboBox.getValue().getId());
        } catch (NullPointerException e) {
            sendNotification("Не все обязательные поля заполнены!");
        }
        return true;
    }

    @Override
    public void setFieldsValues(Recipe entity) {
        descriptionTextArea.setValue(entity.getDescription());
        creationDateField.setValue(convertToLocalDateViaInstant(entity.getCreationDate()));
        expirationDateField.setValue(convertToLocalDateViaInstant(entity.getExpirationDate()));
        doctorComboBox.setValue(entity.getDoctor());
        patientComboBox.setValue(entity.getPatient());
        priorityComboBox.setValue(entity.getPriority());
    }


    @Override
    protected Layout getEditFormView() {
        FormLayout formLayout = new FormLayout();
        formLayout.setSpacing(false);
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
        descriptionTextArea.setMaxLength(200);
        descriptionTextArea.setWidth("100%");
        binder.forField(descriptionTextArea)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите описание.")
                .asRequired()
                .bind(Recipe::getDescription, Recipe::setDescription);

        patientComboBox.setSizeFull();
        doctorComboBox.setSizeFull();
        formLayout.addComponents(patientComboBox, doctorComboBox,
                priorityComboBox, creationDateField, expirationDateField,
                descriptionTextArea);
        return formLayout;
    }

    /**
     * обработка фильтрации таблицы
     *
     * @param event событие для инициаци фильтрации
     */
    private void onFilterChangeEvent(HasValue.ValueChangeEvent<String> event) {
        try {
            ListDataProvider<Recipe> dataProvider = (ListDataProvider<Recipe>) entityGrid.getDataProvider();
            dataProvider.setFilter((gridItem) -> {
                boolean patientFilter = (gridItem.getPatient().getSurname() + " " +
                        gridItem.getPatient().getName())
                        .toLowerCase()
                        .contains(patientFilterField.getValue().toLowerCase());
                boolean descriptionFilter = gridItem.getDescription()
                        .toLowerCase()
                        .contains(descriptionFilterField.getValue().toLowerCase());
                boolean priorityFilter = gridItem.getPriority().getName()
                        .toLowerCase()
                        .contains(priorityFilterField.getValue().toLowerCase());
                return patientFilter && descriptionFilter && priorityFilter;
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * генерация поля ввода данных
     *
     * @param caption заголовок поля
     * @return поле даты для UI
     */
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
}
