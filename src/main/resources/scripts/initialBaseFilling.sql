INSERT INTO PATIENTS (NAME, SURNAME, PATRONYM, PHONE_NUMBER)
        VALUES ('Дмитрий', 'Чуев', 'Вячеславович', '892911122233'),
               ('Александр', 'Семенихин', 'Алексеевич', '89292221133'),
               ('Евгения', 'Селезнева', 'Александровна', '89293332211'),
               ('Оксана', 'Дмитриева', 'Александровна', '89272223311'),
               ('Семён', 'Третьяков', 'Петрович', '89271113322'),
               ('Алексей', 'Копылов', 'Юрьевич', '89171112233'),
               ('Олег', 'Карцев', 'Владимирович', '89172223311'),
               ('Иван', 'Петров', 'Иванович', '89173331122'),
               ('Иван', 'Иванов', 'Иванович', '88005553535');
INSERT INTO DOCTORS (NAME, SURNAME, PATRONYM, SPECIALIZATION)
        VALUES ('Николай', 'Безруков', 'Александрович', 'Хирург'),
               ('Наталья', 'Родина', 'Валентиновна', 'Терапевт'),
               ('Светлана', 'Волкова', 'Сергеевна', 'Терапевт'),
               ('Сергей', 'Красноглазов', 'Николаевич', 'Офтальмолог'),
               ('Владимир', 'Путин', 'Владимирович', 'Обнулёлог');
INSERT INTO PRIORITIES (PRIORITY, NAME)
        VALUES (5,'NORMAL'), (7,'CITO'), (10, 'STATIM');
INSERT INTO RECIPES (DOCTOR_ID, PATIENT_ID, DESCRIPTION, CREATION_DATE, EXPIRATION_DATE, PRIORITY_ID)
        VALUES (0, 0, '*неразборчивые каракули для фармацевта*', '2019-11-19', '2020-12-19', 0),
               (0, 3, 'Кеторол в/м 2 р/сут', '2020-08-15', '2020-08-22', 1),
               (0, 2, 'Воротник шанца + Кеторол таб 2р/сут', '2020-10-19', '2020-11-19', 1),
               (0, 1, 'Противо-воспалительная мазь + Нурофен', '2020-09-19', '2020-11-19', 2),
               (0, 4, 'Купить новые костыли', '2020-08-11', '2020-10-31', 0),
               (4, 5, 'Мазь "Стабильность" втирать в макушку до краха доллара', '2018-03-18', '2024-03-17', 2),
               (4, 6, 'С этим рецептом вы попадете в рай, остальные просто сдохнут', '2020-11-19', '2020-11-19', 2),
               (4, 1, 'Приложить замороженную накопительную часть пенсии к горящему пукану', '2014-01-01', '2022-12-31', 2),
               (4, 0, 'Плотить нологи :)', '2020-09-13', '2021-04-30', 2),
               (4, 3, 'Допрыгнуть на батуте до луны', '2020-11-01', '2020-12-19', 0),
               (2, 4, 'Антигриппин 3р/сут', '2020-07-15', '2020-07-19', 2),
               (2, 2, 'Аспирин 3р/сут', '2020-09-19', '2020-11-19', 1),
               (2, 5, 'Парацетамол 1р/сут, Арбидол 2р/сут', '2020-05-18', '2020-06-05', 2),
               (3, 6, 'Пить антибиотики  против вирусного конъюнктивита', '2020-03-22', '2020-03-31', 0),
               (3, 0, 'Очки для сидения за ПеКа', '2020-04-23', '2020-05-09', 1),
               (3, 1, 'Капать в глаз Визин 3р/сутки', '2020-10-12', '2020-10-19', 2),
               (3, 2, 'Одевать специальные очки для работы за ПК', '2020-07-15', '2020-12-31', 0);