package com.nmh.project.repositories;

import com.nmh.project.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DamageRepository {

    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private Connection connection;

    public DamageRepository(){
        this.connection = DatabaseConnectionManager.getDatabaseConnection();
    }

    public boolean addDamage(String dmgDescription, int motorhomeID){
        //Skal den her metode flyttes til sin egen repository? hvis ja skal getAllDmg() også med
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

    public boolean delDamage(int dmgId){
        //Skal den her metode flyttes til sin egen repository? hvis ja skal getAllDmg() også med
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM damages WHERE damageId = ?");
            statement.setInt(1, dmgId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error in activeMotorhomeRepository : delDamage()");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delDamageByMotorhomeId(int motorhomeId){
        //Skal den her metode flyttes til sin egen repository? hvis ja skal getAllDmg() også med
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM damages WHERE motorhomeDmgId = ?");
            statement.setInt(1, motorhomeId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error in activeMotorhomeRepository : delDamageByMotorhomeId()");
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

    public HashMap<Integer,String> getDmgByMotorhomeId(int id){
        HashMap<Integer,String> damages = new HashMap<>();
        try {
            String getAllString = "SELECT * FROM damages where motorhomeDmgId = ?";
            PreparedStatement statement =  connection.prepareStatement(getAllString);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                damages.put(results.getInt(1), results.getString(2));
            }
        }
        catch (SQLException e){
            System.out.println("error in ActiveMotorhomeRepo : getDmgByMotorhomeId()");
            System.out.println(e.getMessage());
        }
        return damages;
    }
}
