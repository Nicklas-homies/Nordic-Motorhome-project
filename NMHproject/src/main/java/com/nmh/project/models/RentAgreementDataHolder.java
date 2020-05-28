package com.nmh.project.models;

import java.util.Date;

public class RentAgreementDataHolder {
    //NOTE: AUTHORS OF THIS CLASS: JACOB
 //not sure if this is good code. But it seemed easier to save all data here and then get it for the loop. Also seems easier for future updates...

    private int motorhomeId;
    private String brand;
    private String model;
    private int timesUsed;
    private int kmDriven;
    private int price;
    private int typeId;
    private Customer customer;
    private Date startDate;
    private Date endDate;
    private int rentId;

    public RentAgreementDataHolder() {
    }

    public boolean setMotorhomeData (Motorhome motorhome){
        this.motorhomeId = motorhome.getId();
        this.brand = motorhome.getBrand();
        this.model = motorhome.getModel();
        this.kmDriven = motorhome.getKmDriven();
        this.timesUsed = motorhome.getTimesUsed();
        this.typeId = motorhome.getTypeId();
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
