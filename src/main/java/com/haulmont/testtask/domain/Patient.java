package com.haulmont.testtask.domain;

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
}
