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

    public static boolean nameIsValid(String text) {
        String regEx = "^[а-яА-ЯёЁa-zA-Z]{0,30}$";
        return text.matches(regEx) && text.length() > 0;
    }

    public static boolean phoneIsValid(String phoneNumber) {
        String regEx = "^((8|\\+\\d{1})\\d{10})$";
        return phoneNumber.matches(regEx) && phoneNumber.length() > 0;
    }


    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
