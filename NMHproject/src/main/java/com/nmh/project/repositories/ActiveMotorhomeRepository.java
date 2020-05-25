package com.nmh.project.repositories;

import com.nmh.project.models.Motorhome;
import com.nmh.project.models.RentAgreementDataHolder;
import com.nmh.project.util.DatabaseConnectionManager;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ActiveMotorhomeRepository extends MotorhomeRepository{
    private Connection connection;
    CustomerRepository customerRepository = new CustomerRepository();

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
    public ArrayList<Motorhome> filter(int activeState, int typeId, int maxPrice, int minPrice, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        ArrayList<Motorhome> filteredList = returnAvailableMotorhomeByState(activeState);
        filteredList = filterByTypeId(filteredList, typeId);
        filteredList = filterByMaxPrice(filteredList, maxPrice);
        filteredList = filterByMinPrice(filteredList, minPrice);
//        filteredList = filterByStartDate(filteredList, startDate);
//        filteredList = filterByEndDate(filteredList, endDate);
        filteredList = filterByTwoDate(filteredList, startDate, endDate);
        return filteredList;
    }

//    public ArrayList<Motorhome> filterByEndDate(ArrayList<Motorhome> theList, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
//        if (endDate == null){
//            return theList;
//        }
//        ArrayList<Motorhome> found = new ArrayList<>();
//        try {
//            String filterString = "SELECT * FROM motorhomes INNER JOIN custusemotor ON motorhomes.motorhomeId = custusemotor.motorhomeId " +
//                    "WHERE startDate < ? AND endDate > ?";
//            PreparedStatement statement = connection.prepareStatement(filterString);
//
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
//            String currentDateTime = format.format(endDate);
//
//            statement.setString(1, currentDateTime);
//            statement.setString(2,currentDateTime);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()){
//                for (Motorhome home : theList){
//                    if (home.getId() == resultSet.getInt(1)){
//                        found.add(home);
//                    }
//                }
//            }
//        }
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//        theList.removeAll(found);
//        return theList;
//    }
//
//    public ArrayList<Motorhome> filterByStartDate(ArrayList<Motorhome> theList, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate){
//        if (startDate == null){
//            return theList;
//        }
//        ArrayList<Motorhome> found = new ArrayList<>();
//        try {
//            String filterString = "SELECT * FROM motorhomes INNER JOIN custusemotor ON motorhomes.motorhomeId = custusemotor.motorhomeId " +
//                    "WHERE startDate < ? AND endDate > ?";
//            PreparedStatement statement = connection.prepareStatement(filterString);
//
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
//            String currentDateTime = format.format(startDate);
//
//            statement.setString(1, currentDateTime);
//            statement.setString(2,currentDateTime);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()){
//                for (Motorhome home : theList){
//                    if (home.getId() == resultSet.getInt(1)){
//                        found.add(home);
//                    }
//                }
//            }
//        }
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//        theList.removeAll(found);
//        return theList;
//    }

    public ArrayList<Motorhome> filterByTwoDate(ArrayList<Motorhome> theList, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        if (endDate == null && startDate == null){
            return theList;
        }
        ArrayList<Motorhome> found = new ArrayList<>();
        try {
            String filterString = "SELECT * FROM motorhomes INNER JOIN custusemotor ON motorhomes.motorhomeId = custusemotor.motorhomeId" +
                    " WHERE (? between startDate and endDate) OR (? between startDate and endDate) OR (startDate between ? and ?)" +
                    " OR (endDate between ? and ?);";
            PreparedStatement statement = connection.prepareStatement(filterString);

            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            String currentStartDateTime = startDateFormat.format(startDate);

            SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            String currentEndDateTime = endDateFormat.format(endDate);

            statement.setString(1, currentStartDateTime);
            statement.setString(2,currentEndDateTime);
            statement.setString(3,currentStartDateTime);
            statement.setString(4,currentEndDateTime);
            statement.setString(5,currentStartDateTime);
            statement.setString(6,currentEndDateTime);
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

    public ArrayList<Motorhome> filterByTypeId(ArrayList<Motorhome> theList, int typeId){
        if (typeId == -1){
            return theList;
        }
        ArrayList<Motorhome> found = new ArrayList<>();
        try {
            String filterString = "SELECT * FROM motorhomes INNER JOIN motorhometype ON motorhomes.typeId = motorhometype.typeId" +
                    " WHERE motorhometype.typeId != ?";
            PreparedStatement statement = connection.prepareStatement(filterString);
            statement.setInt(1,typeId);
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

    public ArrayList<RentAgreementDataHolder> getActiveMotorhome(){
        ArrayList<RentAgreementDataHolder> allActiveMotorhome = new ArrayList<>();
        try {
            String getActiveMotorhomes = "SELECT * FROM custusemotor";
            PreparedStatement statement = connection.prepareStatement(getActiveMotorhomes);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                RentAgreementDataHolder tempRentAgreementDataHolder = new RentAgreementDataHolder();
                int tempRentId = resultSet.getInt("rentId");
                Motorhome tempMotorhome = read(resultSet.getInt("motorhomeId"));
                //sets data for the holder.
                tempRentAgreementDataHolder.rentId = resultSet.getInt("rentId");
                tempRentAgreementDataHolder.customer = customerRepository.read(resultSet.getInt("customerId"));
                tempRentAgreementDataHolder.setMotorhomeData(tempMotorhome);
                tempRentAgreementDataHolder.startDate = resultSet.getDate("startDate");
                tempRentAgreementDataHolder.endDate = resultSet.getDate("endDate");
                allActiveMotorhome.add(tempRentAgreementDataHolder);
            }
        }
        catch (SQLException e){
            System.out.println("error at getActiveMotorhome");
            System.out.println(e.getMessage());
        }
        return allActiveMotorhome;
    }

    public ArrayList<Double> getFinalPrice(int rentId, HashMap<String, String> allparams){
        ArrayList<Double> prices = new ArrayList<>();
        int tankPrice = 70;
        double kmDropPrice = 0.7;
        double kmDrivenOver = 1;
        double totalPrice = 0;
        int daysOfRent = 0; //hopefully changed in try to something that makes sense.
        try {
            String getTheFinalPrice = "SELECT datediff(endDate,startDate),extraPrice FROM custusemotor WHERE rentId=?";
            PreparedStatement statement = connection.prepareStatement(getTheFinalPrice);
            statement.setInt(1,rentId);
            //need to loop up by rentId and not motorhome ID! Making a new method to fitler right activemotorhome/available
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                totalPrice+=resultSet.getDouble(2);
                daysOfRent = resultSet.getInt(1);
                prices.add(resultSet.getDouble(2));
            }
        }
        catch (SQLException e){
            System.out.println("Error at getFinalPrice()");
            System.out.println(e.getMessage());
        }
        if (allparams.containsKey("tankFull")){ //if tank less than half full this will be here
            totalPrice += tankPrice;
        }
        if (!allparams.get("kmDropDriven").equals("")){  //if not dropped off at offices, this will be here
            int kmDropDriven = Integer.parseInt(allparams.get("kmDropDriven"));
            totalPrice += kmDropPrice * kmDropDriven;
        }
        if (daysOfRent==0){
            daysOfRent = 1;
            //this is if someone rented and returned a motorhome the same day
            //could also indicate error since mysql querry should change this number if it has been rented for more than one day.
        }
        //check km over allowed 400 km per day.
        Motorhome motorhome = read(Integer.parseInt(allparams.get("motorhomeId")));
        int oldKmDriven = motorhome.getKmDriven();
        int newKmDriven = Integer.parseInt(allparams.get("kmDriven"));
        double drivenPerDay = (newKmDriven - oldKmDriven) / daysOfRent;
        if (drivenPerDay > 400){
            totalPrice += (drivenPerDay - 400) * kmDrivenOver;
        }
        prices.add(totalPrice);
        return prices;
    }

    public boolean homeReturned(int rentId){
        try{
            String deleteFromCustusemotor = "DELETE FROM custusemotor WHERE rentId=?";
            PreparedStatement statement = connection.prepareStatement(deleteFromCustusemotor);
            statement.setInt(1,rentId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("error at homeReturned");
            System.out.println(e.getMessage());
        }
        return false;
    }
}
