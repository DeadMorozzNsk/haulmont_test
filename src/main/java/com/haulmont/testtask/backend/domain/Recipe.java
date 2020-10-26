package com.haulmont.testtask.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe implements Entity{
    long id;
    String description;
    long patientId;
    long doctorId;
    Date creationDate;
    Date expirationDate;
    long priorityId;

    Patient patient;
    Doctor doctor;
    Priority priority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id &&
                patientId == recipe.patientId &&
                doctorId == recipe.doctorId &&
                priorityId == recipe.priorityId &&
                description.equals(recipe.description) &&
                creationDate.equals(recipe.creationDate) &&
                expirationDate.equals(recipe.expirationDate) &&
                patient.equals(recipe.patient) &&
                doctor.equals(recipe.doctor) &&
                priority.equals(recipe.priority);
    }
}
