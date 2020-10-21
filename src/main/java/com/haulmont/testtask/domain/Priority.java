package com.haulmont.testtask.domain;

import lombok.Getter;

@Getter
public enum Priority implements Entity {
    NORMAL((short) 5, "Нормальный"),
    CITO((short) 7, "Срочный"),
    STATIM((short) 10, "Немедленный");

    long id;
    int priority;
    String name;

    Priority(int priority, String name) {
        this.priority = priority;
        this.name = name;
    }
}
