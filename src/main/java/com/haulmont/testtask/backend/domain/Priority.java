package com.haulmont.testtask.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Priority implements Entity {
    long id;
    int priority;
    String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Priority priority1 = (Priority) o;
        return id == priority1.id &&
                priority == priority1.priority &&
                name.equals(priority1.name);
    }
}
