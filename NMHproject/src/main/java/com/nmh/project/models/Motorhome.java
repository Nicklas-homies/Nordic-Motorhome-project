package com.nmh.project.models;

public class Motorhome {

    public int id;
    public String brand;
    public String model;
    public int timesUsed;
    public int kmDriven;
    public double extraPrice;
    public int typeId;

    public Motorhome(int id, String brand, String model, int timesUsed, int kmDriven, double extraPrice, int typeId) {
        //will atleast be used when reading from repository
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.timesUsed = timesUsed;
        this.kmDriven = kmDriven;
        this.extraPrice = extraPrice;
        this.typeId = typeId;
    }

    public Motorhome() {
        //used in readAll, then uses setters to set everything.
    }

    @Override
    public String toString() {
        //basic intellij toString();
        return "motorhome{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", timesUsed=" + timesUsed +
                ", kmDriven=" + kmDriven +
                ", extraPrice=" + extraPrice +
                ", typeId=" + typeId +
                '}';
    }

    // Getter and setter ------------------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}