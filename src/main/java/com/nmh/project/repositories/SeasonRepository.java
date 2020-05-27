package com.nmh.project.repositories;

import com.nmh.project.models.Season;
import com.nmh.project.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeasonRepository {

    private Connection connection;

    public SeasonRepository() {
        this.connection = DatabaseConnectionManager.getDatabaseConnection();
    }

    public ArrayList<Season> readAll(){
        ArrayList<Season> allSeasons = new ArrayList<>();
        try{
            String selectAll = "SELECT * FROM season";
            PreparedStatement statement = connection.prepareStatement(selectAll);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                Season tempSeason = new Season();
                tempSeason.setSeasonId(results.getInt(1));
                tempSeason.setStartMonth(results.getInt(2));
                tempSeason.setEndMonth(results.getInt(3));
                tempSeason.setSeasonTypeId(results.getInt(4));
                allSeasons.add(tempSeason);
            }
        }
        catch (SQLException e){
            System.out.println("Error at seasonRepository, readAll()");
            System.out.println(e.getMessage());
        }
        return allSeasons;
    }

    public Season read(int id){
        Season seasonToReturn = new Season();
        try{
            String getById = "SELECT * FROM season WHERE seasonId = ?";
            PreparedStatement statement = connection.prepareStatement(getById);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while(results.next()){
                seasonToReturn.setSeasonId(results.getInt(1));
                seasonToReturn.setStartMonth(results.getInt(2));
                seasonToReturn.setEndMonth(results.getInt(3));
                seasonToReturn.setSeasonTypeId(results.getInt(4));
            }
        }
        catch (SQLException e){
            System.out.println("error at seasonRepository, read()");
            System.out.println(e.getMessage());
        }
        return seasonToReturn;
    }

    public int seasonType(int startMonth, int endMonth){
        ArrayList<Season> allSeasons = readAll();
        int startMonthType = 0;
        int endMonthType = 0;

        for (Season season : allSeasons){
            if (startMonth >= season.getStartMonth() && startMonth <= season.getEndMonth()){
                startMonthType = season.getSeasonTypeId();
            }
        }

        for (Season season : allSeasons){
            if (endMonth >= season.getStartMonth() && endMonth <= season.getEndMonth()){
                endMonthType = season.getSeasonTypeId();
            }
        }

        if (startMonthType < endMonthType){
            return endMonthType;
        }
        return startMonthType;
    }

    public double seasonPrice(int seasonTypeId){
        if (seasonTypeId == 3){
            return 1.6;
        }else if (seasonTypeId == 2){
            return 1.3;
        }else {
            return 1;
        }
    }
}
