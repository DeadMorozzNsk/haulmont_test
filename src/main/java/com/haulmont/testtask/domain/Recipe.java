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
    long doctorId;
    long patientId;
    short priorityId;
    String description;
    Date creationDate;
    Date expirationDate;
}
