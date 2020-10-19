package com.haulmont.testtask.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Person {
    long id;
    String name;
    String surname;
    String patronym;

}
