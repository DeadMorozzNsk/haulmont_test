package com.haulmont.testtask.domain;

import lombok.Getter;

@Getter
public enum Priority implements Entity {
    NORMAL((short) 5, "Нормальный"),
    CITO((short) 7, "Срочный"),
    STATIM((short) 10, "Немедленный");

    long id;
    short priority;
    String name;

    Priority(short priority, String name) {
        this.priority = priority;
        this.name = name;
    }
}
