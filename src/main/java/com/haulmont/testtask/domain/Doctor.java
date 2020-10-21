package com.haulmont.testtask.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Doctor extends Person{
    String specialization;

    public Doctor(long id, String name, String surname, String patronym, String specialization) {
        super(id, name, surname, patronym);
        this.specialization = specialization;
    }

}
