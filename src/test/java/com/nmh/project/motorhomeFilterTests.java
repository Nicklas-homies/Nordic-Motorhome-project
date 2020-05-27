package com.nmh.project;

import com.nmh.project.models.Motorhome;
import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class motorhomeFilterTests {
    //used to test if filtermethod is filtering stuff out correctly.
    //no test of date, since there are no good way to check rentagreements against dates atm. *In trump voice - sad, very sad. huge. it's fakenews

    MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
    ArrayList<Motorhome> allMotorhomes = motorhomeRepository.readAll(); //used in several methods. Should not change since junit has no method dependencies.
    ActiveMotorhomeRepository activeMotorhomeRepository = new ActiveMotorhomeRepository();

    @Test
    void testOfTypeFilter(){
        allMotorhomes = activeMotorhomeRepository.filterByTypeId(allMotorhomes,1);
        for (Motorhome motorhome : allMotorhomes){
            Assertions.assertEquals(1,motorhome.typeId);
        }
    }

    @Test
    void testOfMaxPrice(){
        //hard to test, due to the fact that we don't have a method that returns price by id or type.
        //typeId 8's price is currently 2400. type 7's price is 2000 Per day.
        //if test fails, check prices hasn't been changed.
        allMotorhomes = activeMotorhomeRepository.filterByMaxPrice(allMotorhomes,1900); //should remove type 7 and 8.
        for (Motorhome motorhome : allMotorhomes){
            Assertions.assertFalse(motorhome.typeId == 7 || motorhome.typeId == 8);
        }
    }
}