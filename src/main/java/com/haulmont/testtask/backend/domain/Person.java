package com.haulmont.testtask.backend.domain;

import com.vaadin.data.ValueProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Person implements Entity{
    long id;
    String name;
    String surname;
    String patronym;

    public abstract String getPersonField();
    public abstract void setPersonField(String value);

    public Person() {
    }

    public Person(long id, String name, String surname, String patronym) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronym = patronym;
    }
}
