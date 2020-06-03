package com.nmh.project.repositories;

import com.nmh.project.models.Customer;

import java.util.List;

public interface ICustomerRepo {

    public int create(Customer customer);

    public Customer read(int id);

    public List<Customer> readAll();

    public boolean update(Customer customer);

    public boolean delete(int id);

}
