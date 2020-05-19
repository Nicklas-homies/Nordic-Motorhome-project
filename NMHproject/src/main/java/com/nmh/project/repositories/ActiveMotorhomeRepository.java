package com.nmh.project.repositories;

import com.nmh.project.models.Motorhome;
import com.nmh.project.util.DatabaseConnectionManager;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ActiveMotorhomeRepository extends MotorhomeRepository{
    private Connection connection;

    public ActiveMotorhomeRepository(){
            this.connection = DatabaseConnectionManager.getDatabaseConnection();
    }

    public boolean addDamage(String dmgDescription, int motorhomeID){
        //Skal den her metode flyttes til sin egen repository?
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO damages(damageDesc, motorhomeDmgId) VALUES (?,?)");
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
    public ArrayList<Motorhome> availableByDate(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
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
        ArrayList<Motorhome> motorhomeAvailableByDate = new ArrayList<>();
        for (int id : motorhomeIdAvaible){
            motorhomeAvailableByDate.add(read(id));
        }
        return motorhomeAvailableByDate;
    }

    public boolean rentHome(int motorhomeID, int customerID, Date startDate, Date endDate){
        //warning warning:
        //  THERE SHOULD BE A CHECK IF THE HOME IS AVAILABLE AT THE GIVEN TIME!!! CURRENTLY THERE ISNt!
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

}
