package com.nmh.project;

import com.nmh.project.models.Motorhome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NmHprojectApplicationTests {


    @Test
    void contextLoads() {
    }

    @Test
    void assertTest(){
        Assertions.assertEquals(2,2);
    }

    @Test
    void assertTest3(){
        Assertions.assertEquals(4,1);
    }

    @Test
    public void assertTest4(){
        Motorhome motorhome = new Motorhome();
        Assertions.assertEquals(null,motorhome.brand);
    }
}
