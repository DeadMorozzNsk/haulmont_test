package com.haulmont.testtask.ui.components;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public abstract class Validator {
    public static final String MODAL_WINDOW = "modal-window";
    public static final String VIEW_LAYOUT = "view-layout";
    public static final String HEADER_LAYOUT = "header-layout";
    public static final String BORDERLESS = "borderless";

    /**
     * проверка строки на соответствие регулярному выражению
     * для ФИО
     *
     * @param text строка для проверки
     * @return результат проверки
     */
    public static boolean nameIsValid(String text) {
        String regEx = "^[а-яА-ЯёЁa-zA-Z]{0,30}$";
        return text.matches(regEx);
    }

    /**
     * проверка строки на соответствие регулярному выражению
     * для номера телефона
     *
     * @param phoneNumber строка для проверки
     * @return результат проверки
     */
    public static boolean phoneIsValid(String phoneNumber) {
        String regEx = "^((8|\\+\\d{1})\\d{10})$";
        return phoneNumber.matches(regEx);
    }


    /**
     * конвертация даты Date -> LocalDate
     *
     * @param dateToConvert Дата для конвертации
     * @return сконвертированная дата
     */
    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * конвертация даты Date -> LocalDate
     *
     * @param dateToConvert Дата для конвертации
     * @return сконвертированная дата
     */
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
