package com.nmh.project.repositories;

import com.nmh.project.models.Customer;
import com.nmh.project.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerRepository implements ICustomerRepo {
    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private Connection connection;

    public CustomerRepository() {
        this.connection = DatabaseConnectionManager.getDatabaseConnection();
    }

    @Override
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

    @Override
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

    @Override
    public int create(Customer customer){
        int customerId = -1;
        try {
            String insertString = "INSERT INTO customers (cName, number) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(insertString);
            statement.setString(1,customer.getcName());
            statement.setInt(2,customer.getNumber());
            statement.executeUpdate();

            //gets last customer Id used for tests
            String getLastInsertedId = "SELECT customerId FROM customers WHERE customerId=(SELECT LAST_INSERT_ID())";
            PreparedStatement statement1 = connection.prepareStatement(getLastInsertedId);
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                customerId = resultSet.getInt(1);
            }

            return customerId;
        }
        catch (SQLException e){
            System.out.println("error at create() customerRepository");
            System.out.println(e.getMessage());
        }
        return customerId;
    }

    @Override
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

    @Override
    public boolean update (Customer customer){
        try{
            String update = "UPDATE customers SET cName=?, number=? WHERE customerId=?";
            PreparedStatement updateStatement = connection.prepareStatement(update);
            updateStatement.setString(1, customer.getcName());
            updateStatement.setInt(2, customer.getNumber());
            updateStatement.setInt(3, customer.getId());
            updateStatement.executeUpdate();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
