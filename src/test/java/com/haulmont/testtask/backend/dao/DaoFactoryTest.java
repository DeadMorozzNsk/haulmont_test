package com.haulmont.testtask.backend.dao;

import com.haulmont.testtask.backend.dao.exceptions.DaoException;
import com.haulmont.testtask.backend.domain.Doctor;
import com.haulmont.testtask.backend.domain.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DaoFactoryTest {

    @Test
    public void testFactoryInstance() {
        DaoFactory[] factories = new DaoFactory[5];
        for (int i = 0; i < 5; i++) {
            factories[i] = DaoFactory.getInstance();
        }
        for (int i = 0; i < 4; i++) {
            Assertions.assertEquals(factories[i], factories[i + 1]);
        }
    }

    @Test
    public void testDaoTypes() {
        try {
            List<? extends Entity> list = DaoFactory.getInstance().getDaoByType(DaoEntityType.DAO_DOCTOR).getAll();
            boolean test = list instanceof ArrayList;
            Assertions.assertTrue(test);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getEntities() {
        List<Doctor> list = new ArrayList<>();
      /* выгрузка из базы
        0,"Николай","Безруков","Александрович","Хирург"
        1,"Наталья","Родина","Валентиновна","Терапевт"
        2,"Светлана","Волкова","Сергеевна","Терапевт"
        3,"Сергей","Красноглазов","Николаевич","Офтальмолог"
        7,"Тест","Тестовый","Тестович","Тестолог"
        9,"Лекарь","Аптечный","Айболитович","Анестезиолог"
        10,"kjlkjlkj","Фывфывфыв","фыйцуцйуцй","докторрррррррррррр"*/
        list.add(new Doctor(0, "Николай", "Безруков", "Александрович", "Хирург"));
        list.add(new Doctor(1, "Наталья", "Родина", "Валентиновна", "Терапевт"));
        list.add(new Doctor(2, "Светлана", "Волкова", "Сергеевна", "Терапевт"));
        list.add(new Doctor(3, "Сергей", "Красноглазов", "Николаевич", "Офтальмолог"));
        list.add(new Doctor(7, "Тест", "Тестовый", "Тестович", "Тестолог"));
        list.add(new Doctor(9, "Лекарь", "Аптечный", "Айболитович", "Анестезиолог"));
        list.add(new Doctor(10, "kjlkjlkj", "Фывфывфыв", "фыйцуцйуцй", "докторрррррррррррр"));

        List<Doctor> doctorList = null;
        try {
            doctorList = DaoFactory.getInstance().getDaoDoctor().getAll();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(doctorList);
        for (int i = 0; i < doctorList.size(); i++) {
            Assertions.assertEquals(list.get(i), doctorList.get(i));
        }
    }
}
