package com.nmh.project;

import com.nmh.project.models.Customer;
import com.nmh.project.models.Motorhome;
import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.CustomerRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class rentAHomeTest {
    //NOTE: AUTHORS OF THIS CLASS: JACOB

    @Test
    void testOfRentHomeAndReturnHome() throws ParseException {
        //1 method instead of 2 because tests can't have dependencies on each other, and I can't send rentId to another test.
        MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
        ActiveMotorhomeRepository activeMotorhomeRepository = new ActiveMotorhomeRepository();
        CustomerRepository customerRepository = new CustomerRepository();

        Motorhome motorhome = motorhomeRepository.readAll().get(0);  //in case home with specific id is deleted, slower than using read(id) but safer.
        Customer customer = customerRepository.readAll().get(0);

        String stringStartdate = "24-05-2039"; //not testing if available
        String stringEndDate = "29-06-2039";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = format.parse(stringStartdate);
        Date endDate = format.parse(stringEndDate);

        int rentId = activeMotorhomeRepository.rentHome(motorhome.getId(),customer.getId(),startDate,endDate);
        Assertions.assertNotEquals(-1,rentId);

        Assertions.assertTrue(activeMotorhomeRepository.homeReturned(rentId,motorhome.getKmDriven()));

    }
}