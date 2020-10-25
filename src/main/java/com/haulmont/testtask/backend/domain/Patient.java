package com.haulmont.testtask.backend.domain;

import com.vaadin.data.ValueProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Patient extends Person {

    String phoneNumber;

    public Patient(long id, String name, String surname, String patronym, String phoneNumber) {
        super(id, name, surname, patronym);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getPersonField() {
        return phoneNumber;
    }

    @Override
    public void setPersonField(String value) {
        this.phoneNumber = value;
    }
}
