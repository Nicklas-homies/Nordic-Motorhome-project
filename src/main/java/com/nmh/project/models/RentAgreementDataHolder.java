package com.nmh.project.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.util.Date;

public class RentAgreementDataHolder {
 //not sure if this is good code. But it seemed easier to save all data here and then get it for the loop. Also seems easier for future updates...

    public int motorhomeId;
    public String brand;
    public String model;
    public int timesUsed;
    public int kmDriven;
    public int price;
    public int activeState;
    public int typeId;
    public Customer customer;
    public Date startDate;
    public Date endDate;
    public int rentId;

    public RentAgreementDataHolder() {
    }



    public boolean setMotorhomeData (Motorhome motorhome){
        this.motorhomeId = motorhome.id;
        this.brand = motorhome.brand;
        this.model = motorhome.model;
        this.kmDriven = motorhome.kmDriven;
        this.timesUsed = motorhome.timesUsed;
        this.typeId = motorhome.typeId;
        return true;
    }

    public int getId() {
        return motorhomeId;
    }

    public void setId(int motorhomeId) {
        this.motorhomeId = motorhomeId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public int getKmDriven() {
        return kmDriven;
    }

    public void setKmDriven(int kmDriven) {
        this.kmDriven = kmDriven;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getActiveState() {
        return activeState;
    }

    public void setActiveState(int activeState) {
        this.activeState = activeState;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getRentId() {
        return rentId;
    }

    public void setRentId(int rentId) {
        this.rentId = rentId;
    }
}
