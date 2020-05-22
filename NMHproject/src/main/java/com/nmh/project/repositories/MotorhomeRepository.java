package com.nmh.project.repositories;

import com.nmh.project.models.Motorhome;
import com.nmh.project.util.DatabaseConnectionManager;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MotorhomeRepository {
    private Connection conn;

    public MotorhomeRepository() {
        this.conn = DatabaseConnectionManager.getDatabaseConnection();
    }

    public ArrayList<Motorhome> returnAvailableMotorhomeByState(int activeState){
        ArrayList<Motorhome> tempAllMotorhome = readAll();
        ArrayList<Motorhome> tempHome = new ArrayList<>();
        for (Motorhome motorhome : tempAllMotorhome) {
            if (motorhome.getActiveState() == activeState) {
                tempHome.add(motorhome);
            }
        }
        return tempHome;
    }

    public ArrayList<Motorhome> readAll(){
        ArrayList<Motorhome> allHomes = new ArrayList<>();
        try{
            String selectAll = "SELECT * FROM motorhomes";
            PreparedStatement statement = conn.prepareStatement(selectAll);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                Motorhome tempHome = new Motorhome();
                tempHome.setId(results.getInt(1));
                tempHome.setBrand(results.getString(2));
                tempHome.setModel(results.getString(3));
                tempHome.setTimesUsed(results.getInt(4));
                tempHome.setKmDriven(results.getInt(5));
                tempHome.setActiveState(results.getInt(6));
                tempHome.setTypeId(results.getInt(7));
                allHomes.add(tempHome);
            }
        }
        catch (SQLException e){
            System.out.println("Error at motorhomeRepository, readAll()");
            System.out.println(e.getMessage());
        }
        return allHomes;
    }

    public Motorhome read(int id){
        Motorhome homeToReturn = new Motorhome();
        try{
            String getById = "SELECT * FROM motorhomes WHERE motorhomeId=?";
            PreparedStatement statement = conn.prepareStatement(getById);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while(results.next()){
                homeToReturn.setId(results.getInt(1));
                homeToReturn.setBrand(results.getString(2));
                homeToReturn.setModel(results.getString(3));
                homeToReturn.setTimesUsed(results.getInt(4));
                homeToReturn.setKmDriven(results.getInt(5));
                homeToReturn.setActiveState(results.getInt(6));
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
            String insertString = "INSERT INTO motorhomes (brand,model,timesUsed,kmDriven,extraPrice,typeId) VALUES (?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insertString);
            statement.setString(1,motorhome.getBrand());
            statement.setString(2,motorhome.getModel());
            statement.setInt(3,motorhome.getTimesUsed());
            statement.setInt(4,motorhome.getKmDriven());
            statement.setInt(5,motorhome.getTypeId());
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
            PreparedStatement statement = conn.prepareStatement(deleteString);
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

    public boolean update(Motorhome motorhome) {
        try{
            String update = "UPDATE motorhomes SET brand=?,model=?,timesUsed=?,kmDriven=?,extraPrice=?,activeState=?,typeId=? WHERE motorhomeId=?";
            PreparedStatement updateStatement = conn.prepareStatement(update);
            updateStatement.setString(1, motorhome.getBrand());
            updateStatement.setString(2, motorhome.getModel());
            updateStatement.setInt(3, motorhome.getTimesUsed());
            updateStatement.setInt(4, motorhome.getKmDriven());
            updateStatement.setInt(5, motorhome.getActiveState());
            updateStatement.setInt(6, motorhome.getTypeId());
            updateStatement.setInt(7,motorhome.getId());
            updateStatement.executeUpdate();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
