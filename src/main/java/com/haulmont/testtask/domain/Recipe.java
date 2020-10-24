package com.haulmont.testtask.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
}
