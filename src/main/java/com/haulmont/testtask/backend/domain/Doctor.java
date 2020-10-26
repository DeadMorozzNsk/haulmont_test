package com.haulmont.testtask.backend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Doctor extends Person {
    String specialization;

    public Doctor(long id, String name, String surname, String patronym, String specialization) {
        super(id, name, surname, patronym);
        this.specialization = specialization;
    }

    @Override
    public String getPersonField() {
        return specialization;
    }

    @Override
    public void setPersonField(String value) {
        this.specialization = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return specialization.equals(doctor.specialization);
    }
}
