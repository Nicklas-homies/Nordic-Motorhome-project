package com.nmh.project.repositories;

import com.nmh.project.models.Motorhome;
import com.nmh.project.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MotorhomeRepository implements IMotorhomeRepo {
    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private Connection connection;

    public MotorhomeRepository() {
        this.connection = DatabaseConnectionManager.getDatabaseConnection();
    }
    private SeasonRepository seasonRepository = new SeasonRepository();

    @Override
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

    @Override
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

    @Override
    public boolean create(Motorhome motorhome){
        try {
            String insertString = "INSERT INTO motorhomes (brand,model,timesUsed,kmDriven,typeId) VALUES (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertString);
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

    @Override
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

    @Override
    public boolean update(Motorhome motorhome) {
        try{
            String update = "UPDATE motorhomes SET brand=?,model=?,timesUsed=?,kmDriven=?,activeState=?,typeId=? WHERE motorhomeId=?";
            PreparedStatement updateStatement = connection.prepareStatement(update);
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

    public double getInitialPrice(Motorhome motorhome, HashMap<String,String> ekstraStuff, Date startDate, Date endDate){

        double totalPrice = 0; //price is in whole euro
        double dayPrice = 0;
        long rentedDays = ChronoUnit.DAYS.between(startDate.toInstant(),endDate.toInstant());

        try {
            String getDayPrice = "SELECT price FROM motorhomes INNER JOIN motorhometype ON" +
                    " motorhomes.typeId = motorhometype.typeId WHERE motorhomes.motorhomeId = ?";
            PreparedStatement statement = connection.prepareStatement(getDayPrice);
            statement.setInt(1,motorhome.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                dayPrice = resultSet.getDouble("price");
            }
        }
        catch (SQLException e){
            System.out.println("error in MotorhomeRepository : at getInitialPrice()");
            System.out.println(e.getMessage());
        }

        dayPrice *= seasonRepository.seasonPrice(seasonRepository.seasonType(startDate.getMonth() + 1, endDate.getMonth() + 1));

        totalPrice += (dayPrice * rentedDays);

        if (ekstraStuff.containsKey("bedLinen")){
            totalPrice += 15;
        }
        if (ekstraStuff.containsKey("bikeRack")){
            totalPrice += 25;
        }
        if (ekstraStuff.containsKey("childSeat")){
            totalPrice += 20;
        }
        if (ekstraStuff.containsKey("picnicTable")){
            totalPrice += 20;
        }
        if (ekstraStuff.containsKey("chairs")){
            totalPrice += 20;
        }

        return totalPrice;
    }

    public boolean newRentDeal(Date startDate, Date endDate, double price, int customerId, int motorhomeId){
        try {
            String insertRentDeal = "INSERT INTO custusemotor(startDate, endDate, extraPrice, customerId, motorhomeId) VALUES (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertRentDeal);
            statement.setDate(1,new java.sql.Date(startDate.getTime()));
            statement.setDate(2,new java.sql.Date(endDate.getTime()));
            statement.setDouble(3,price);
            statement.setInt(4,customerId);
            statement.setInt(5,motorhomeId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("newRentDeal error");
            System.out.println(e.getMessage());
        }

    return false;
    }
}
