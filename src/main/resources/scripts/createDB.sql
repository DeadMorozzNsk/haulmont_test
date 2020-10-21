drop table PATIENTS if exists;
drop table DOCTORS if exists;
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
    PATRONYM          varchar(50) not null,
    SPECIALIZATION    varchar(50) not null
);
--приоритеты
create table PRIORITIES
(
    ID  bigint identity
        constraint PRIORITIES_PK
            primary key,
    PRIORITY int not null,
    NAME varchar(16) not null
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