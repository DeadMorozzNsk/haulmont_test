package com.haulmont.testtask.dao;

import com.haulmont.testtask.backend.dao.DaoFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaoFactoryTest {

    @Test
    public void testFactoryInstance(){
        DaoFactory[] factories = new DaoFactory[5];
        for (int i = 0; i < 5; i++) {
            factories[i] = DaoFactory.getInstance();
        }
        for (int i = 0; i < 4; i++) {
            Assertions.assertEquals(factories[i],factories[i+1]);
        }
    }
}
