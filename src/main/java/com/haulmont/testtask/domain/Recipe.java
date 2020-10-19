package com.haulmont.testtask.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Recipe {
    long id;
    long doctorId;
    long patientId;
    short priorityId;
    String description;
    Date creationDate;
    Date expirationDate;
}
