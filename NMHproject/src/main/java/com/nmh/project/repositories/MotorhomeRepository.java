package com.nmh.project.repositories;

import com.nmh.project.models.Motorhome;
import com.nmh.project.util.DatabaseConnectionManager;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MotorhomeRepository {
//todo: Split up this repository into smaller repositories, as said in design diagrams.
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

    // Filter methods, should they be in a different class? how do we do this when thinking of grasp and smart design?
    public ArrayList<Motorhome> filter(int maxPrice, int minPrice, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        ArrayList<Motorhome> filteredList = readAll();
        filteredList = filterByMaxPrice(filteredList,maxPrice);
        filteredList = filterByMinPrice(filteredList,minPrice);
        filteredList = filterByStartDate(filteredList,startDate);
        filteredList = filterByEndDate(filteredList,endDate);

        return filteredList;
    }

    public ArrayList<Motorhome> filterByEndDate(ArrayList<Motorhome> theList, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        if (endDate == null){
            return theList;
        }
        ArrayList<Motorhome> found = new ArrayList<>();
        try {
            String filterString = "SELECT * FROM motorhomes INNER JOIN custusemotor ON motorhomes.motorhomeId = custusemotor.motorhomeId " +
                    "WHERE ? < startDate OR ? > endDate";
            PreparedStatement statement = connection.prepareStatement(filterString);
            statement.setDate(1,new java.sql.Date(endDate.getTime()));
            statement.setDate(2,new java.sql.Date(endDate.getTime()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                for (Motorhome home : theList){
                    if (home.getId() == resultSet.getInt(1)){
                        found.add(home);
                    }
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        theList.removeAll(found);
        return theList;
    }

    public ArrayList<Motorhome> filterByStartDate(ArrayList<Motorhome> theList, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate){
        if (startDate == null){
            return theList;
        }
        ArrayList<Motorhome> found = new ArrayList<>();
        try {
            String filterString = "SELECT * FROM motorhomes INNER JOIN custusemotor ON motorhomes.motorhomeId = custusemotor.motorhomeId " +
                    "WHERE startDate < ? OR endDate > ?";
            PreparedStatement statement = connection.prepareStatement(filterString);
            statement.setDate(1,new java.sql.Date(startDate.getTime()));
            statement.setDate(2,new java.sql.Date(startDate.getTime()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                for (Motorhome home : theList){
                    if (home.getId() == resultSet.getInt(1)){
                        found.add(home);
                    }
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        theList.removeAll(found);
        return theList;
    }

    public ArrayList<Motorhome> filterByMinPrice(ArrayList<Motorhome> theList, int minPrice){
        if (minPrice == -1){
            return theList;
        }
        ArrayList<Motorhome> found = new ArrayList<>();
        try {
            String filterString = "SELECT * FROM motorhomes INNER JOIN motorhometype ON motorhomes.typeId = motorhometype.typeId" +
                    " WHERE motorhometype.price < ?"; //finds stuff under min price
            PreparedStatement statement = connection.prepareStatement(filterString);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                for (Motorhome home : theList){
                    if (home.getId() == resultSet.getInt(1)){
                        found.add(home);
                    }
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        theList.removeAll(found); //remove found homes.
        return theList;
    }

    public ArrayList<Motorhome> filterByMaxPrice(ArrayList<Motorhome> theList, int maxPrice){
        if (maxPrice == -1){
            return theList;
        }
        ArrayList<Motorhome> found = new ArrayList<>();
        try {
            String filterString = "SELECT * FROM motorhomes INNER JOIN motorhometype ON motorhomes.typeId = motorhometype.typeId" +
                    " WHERE motorhometype.price > ?";
            //finds all that are over price
            PreparedStatement statement = connection.prepareStatement(filterString);
            statement.setInt(1,maxPrice);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                for (Motorhome home : theList){ //has to go through loop, because the list might have been modified by other filters.
                    if (home.getId() == resultSet.getInt(1)){
                        found.add(home);
                    }
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        theList.removeAll(found);
        return theList;
    }

    // måske nedenstående metode aldrig skal bruges, fordi man altid vil bruge filter metoden?
    public ArrayList<Motorhome> avaibleByDate(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        ArrayList<Integer> motorhomeIdAvaible = new ArrayList<>();
        try {
            String getString = "SELECT * FROM custusemotor WHERE (startDate < ? OR startDate > ?) and (endDate < ? OR endDate > ?)";
            PreparedStatement statement = connection.prepareStatement(getString);
            statement.setDate(1,new java.sql.Date(startDate.getTime()));
            statement.setDate(2,new java.sql.Date(endDate.getTime()));
            statement.setDate(3,new java.sql.Date(startDate.getTime()));
            statement.setDate(4,new java.sql.Date(endDate.getTime()));
            ResultSet results = statement.executeQuery();
            while (results.next()){
                motorhomeIdAvaible.add(results.getInt("motorhomeId"));
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        ArrayList<Motorhome> motorhomeAvaibleByDate = new ArrayList<>();
        for (int id : motorhomeIdAvaible){
            motorhomeAvaibleByDate.add(read(id));
        }
        return motorhomeAvaibleByDate;
    }

    public boolean rentHome(int motorhomeID, int customerID, Date startDate, Date endDate){
        //warning warning:
        //  THERE SHOULD BE A CHECK IF THE HOME IS AVAIBLE AT THE GIVEN TIME!!! CURRENTLY THERE ISNt!
        try {
            String insertString = "INSERT INTO custusemotor(startDate, endDate, customerId, motorhomeId) values (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertString);
            statement.setInt(3, customerID);
            statement.setInt(4,motorhomeID);
            statement.setDate(1,new java.sql.Date(startDate.getTime()));
            statement.setDate(2,new java.sql.Date(endDate.getTime()));
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error at rentHome");
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
