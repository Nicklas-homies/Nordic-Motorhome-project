package com.nmh.project;


import com.nmh.project.models.Customer;
import com.nmh.project.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class customerTests {
    //NOTE: AUTHORS OF THIS CLASS: JACOB
//this test tests if you can Rent a home and return it. Makes a test customer and such on the way, since you need a customer etc. to rent soemthing.


    CustomerRepository customerRepository = new CustomerRepository(); //is here because will be used more places.



    @Test
    void testEmptyCustomerCreation(){

        Customer testCustomer = new Customer();
        Assertions.assertNotNull(testCustomer); //Is this even needed? Since if this fails, program propable crashes since there would be error in constructor
        boolean customerCreated = customerRepository.create(testCustomer);
        //should be fail since we don't want customers without information uploaded to our database.
        Assertions.assertTrue(customerCreated); //will be false if SQL failed.

    }

    @Test
    void testCustomerCreateAndDelete(){
        //testing both create and delete so we don't have a test customer floating in our database.
        boolean customerId;
        Customer testCustomer = new Customer();
        testCustomer.setcName("FrankenTESTin");
        testCustomer.setNumber(12345678);

        Assertions.assertTrue(customerRepository.create(testCustomer)); //is false if SQL failed

        Assertions.assertTrue(customerRepository.delete(testCustomer.getId()));
    }

    @Test
    void testOfReadAll(){
        //will fail if no customers in database!
        ArrayList<Customer> readAllCustomer = customerRepository.readAll();
        Assertions.assertNotEquals(0,readAllCustomer.size());

    }

}