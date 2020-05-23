package com.nmh.project.repositories;

import com.nmh.project.models.Customer;
import com.nmh.project.models.Motorhome;
import com.nmh.project.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerRepository {

    private Connection connection;

    public CustomerRepository() {
        this.connection = DatabaseConnectionManager.getDatabaseConnection();
    }

    public ArrayList<Customer> readAll(){
        ArrayList<Customer> allCust = new ArrayList<>();
        try{
            String selectAll = "SELECT * FROM customers";
            PreparedStatement statement = connection.prepareStatement(selectAll);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                Customer tempCust = new Customer();
                tempCust.setId(results.getInt(1));
                tempCust.setcName(results.getString(2));
                tempCust.setNumber(results.getInt(3));
                allCust.add(tempCust);
            }
        }
        catch (SQLException e){
            System.out.println("Error at customerRepository, readAll()");
            System.out.println(e.getMessage());
        }
        return allCust;
    }

    public Customer read(int id){
        Customer custToReturn = new Customer();
        try{
            String getById = "SELECT * FROM customers WHERE customerId=?";
            PreparedStatement statement = connection.prepareStatement(getById);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while(results.next()){
                custToReturn.setId(results.getInt(1));
                custToReturn.setcName(results.getString(2));
                custToReturn.setNumber(results.getInt(3));
            }
        }
        catch (SQLException e){
            System.out.println("error at customerRepository, read()");
            System.out.println(e.getMessage());
        }
        return custToReturn;
    }

    public int create(Customer customer){
        try {
            String insertString = "INSERT INTO customers (cName, number) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(insertString);
            statement.setString(1,customer.getcName());
            statement.setInt(2,customer.getNumber());
            statement.executeUpdate();

            String selectLast = "SELECT LAST_INSERT_ID()";
            PreparedStatement statement1 =  connection.prepareStatement(selectLast);
            ResultSet resultSet = statement1.executeQuery();
            int id = -1;
            while (resultSet.next()){
                id = resultSet.getInt(1);
            }
            return id;
        }
        catch (SQLException e){
            System.out.println("error at create() customerRepository");
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean delete(int id){
        String deleteString = "DELETE FROM customers WHERE customerId=?";
        try {
            PreparedStatement statement = connection.prepareStatement(deleteString);
            statement.setInt(1,id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error : customerRepository delete()");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean update (Customer customer){

        return true;
    }
}
