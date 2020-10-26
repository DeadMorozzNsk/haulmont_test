package com.haulmont.testtask.backend.domain;

import com.vaadin.data.ValueProvider;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                name.equals(person.name) &&
                surname.equals(person.surname) &&
                patronym.equals(person.patronym);
    }
}
