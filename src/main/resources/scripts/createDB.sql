drop table PATIENTS if exists;
drop table DOCTORS if exists;
drop table SPECIALIZATIONS if exists;
drop table DOCTORS_SPECS if exists;
drop table PRIORITIES if exists;
drop table RECIPES if exists;
--пациенты
create table PATIENTS
(
    ID  bigint identity
        constraint PATIENTS_PK
            primary key,
    NAME         varchar(50) not null,
    SURNAME      varchar(50) not null,
    PATRONYM     varchar(50) not null,
    PHONE_NUMBER varchar(20) not null
);
--доктора
create table DOCTORS
(
    ID  bigint identity
        constraint DOCTORS_PK
            primary key,
    NAME              varchar(50) not null,
    SURNAME           varchar(50) not null,
    PATRONYM        varchar(50) not null
);
--специализации
create table SPECIALIZATIONS
(
    ID  bigint identity
        constraint SPECIALIZATIONS_SPECS_PK
            primary key,
    NAME varchar(50) not null
);
--мэппинг докторов и специализаций, т.к. у одного доктора может быть более 1 специализации
create table DOCTORS_SPECS
(
    ID  bigint identity
        constraint DOCTORS_SPECS_PK
            primary key,
    DOCTOR_ID bigint not null
        constraint DOCTORS_SPECS_DOCTORS_ID_FK
            references DOCTORS(ID),
    SPECIALIZATION_ID bigint not null
        constraint DOCTORS_SPECS_SPECIALIZATIONS_ID_FK
            references SPECIALIZATIONS(ID)
);
--приоритеты
create table PRIORITIES
(
    ID  bigint identity
        constraint PRIORITIES_PK
            primary key,
    NAME varchar(16)
);
--рецепты
create table RECIPES
(
    ID  bigint identity
        constraint RECIPES_PK
            primary key,
    DESCRIPTION     varchar(200) not null,
    PATIENT_ID      bigint not null
        constraint RECIPES_PATIENTS_ID_FK
            references PATIENTS(ID),
    DOCTOR_ID       bigint not null
        constraint RECIPES_DOCTORS_ID_FK
            references DOCTORS(ID),
    CREATION_DATE   date not null,
    EXPIRATION_DATE date not null,
    PRIORITY_ID     bigint not null
        constraint RECIPES_PRIORITIES_ID_FK
            references PRIORITIES(ID)
);