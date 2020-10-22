package com.haulmont.testtask.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Person implements Entity, EntityFront{
    long id;
    String name;
    String surname;
    String patronym;

    public Person() {
    }

    public Person(long id, String name, String surname, String patronym) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronym = patronym;
    }
}
