package com.nmh.project.repositories;

import com.nmh.project.models.Motorhome;
import com.nmh.project.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MotorhomeRepository {

    private Connection connection;

    public MotorhomeRepository() {
        this.connection = DatabaseConnectionManager.getDatabaseConnection();
    }

    public boolean addDamage(String dmgDescription, int motorhomeID){
        //Skal den her metode flyttes til sin egen repository?
        try {
            String addString = "INSERT INTO damages(damageDesc, motorhomeDmgId) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(addString);
            statement.setInt(2,motorhomeID);
            statement.setString(1,dmgDescription);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error : motorhomeRepository addDamage");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public HashMap<Integer,String> getAllDmg(){
        HashMap<Integer,String> damages = new HashMap<>();
        try {
            String getAllString = "SELECT * FROM damages";
            PreparedStatement statement =  connection.prepareStatement(getAllString);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                damages.put(results.getInt(3),results.getString(2));
            }
        }
        catch (SQLException e){
            System.out.println("error : getAllDmg()");
            System.out.println(e.getMessage());
        }
        return damages;
    }

    public ArrayList<Motorhome> readAll(){
        ArrayList<Motorhome> allHomes = new ArrayList<>();
        try{
            String selectAll = "SELECT * FROM motorhomes";
            PreparedStatement statement = connection.prepareStatement(selectAll);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                Motorhome tempHome = new Motorhome();
                tempHome.setId(results.getInt(1));
                tempHome.setBrand(results.getString(2));
                tempHome.setModel(results.getString(3));
                tempHome.setTimesUsed(results.getInt(4));
                tempHome.setKmDriven(results.getInt(5));
                tempHome.setExtraPrice(results.getDouble(6));
                tempHome.setTypeId(results.getInt(7));
                allHomes.add(tempHome);
            }
        }
        catch (SQLException e){
            System.out.println("Error at motorhomerRepository, readAll()");
            System.out.println(e.getMessage());
        }
        return allHomes;
    }

    public Motorhome read(int id){
        Motorhome homeToReturn = new Motorhome();
        try{
            String getById = "SELECT * FROM motorhomes WHERE motorhomeId=?";
            PreparedStatement statement = connection.prepareStatement(getById);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while(results.next()){
                homeToReturn.setId(results.getInt(1));
                homeToReturn.setBrand(results.getString(2));
                homeToReturn.setModel(results.getString(3));
                homeToReturn.setTimesUsed(results.getInt(4));
                homeToReturn.setKmDriven(results.getInt(5));
                homeToReturn.setExtraPrice(results.getDouble(6));
                homeToReturn.setTypeId(results.getInt(7));
            }
        }
        catch (SQLException e){
            System.out.println("error at motorhomeRepository");
            System.out.println(e.getMessage());
        }
        return homeToReturn;
    }

    public boolean create(Motorhome motorhome){
        try {
            String insertString = "INSERT INTO motorhomes VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertString);
            statement.setInt(1,motorhome.getId());
            statement.setString(2,motorhome.getBrand());
            statement.setString(3,motorhome.getModel());
            statement.setInt(4,motorhome.getTimesUsed());
            statement.setInt(5,motorhome.getKmDriven());
            statement.setDouble(6,motorhome.getExtraPrice());
            statement.setInt(7,motorhome.getTypeId());
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error at create() motorhomeRepository");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delete(int id){
        String deleteString = "DELETE FROM motorhomes WHERE motorhomeId=?";
        try {
            PreparedStatement statement = connection.prepareStatement(deleteString);
            statement.setInt(1,id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error : motorhomeRepository delete()");
            System.out.println(e.getMessage());
        }
        return false;
    }

}
