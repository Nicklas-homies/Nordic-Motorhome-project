package com.nmh.project.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Motorhome {
    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private int id;
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
    @NotNull
    private int timesUsed;
    @NotNull
    private int kmDriven;
    private int price;
    private int activeState;
    @NotNull
    @Min(1)
    @Max(8)
    private int typeId;

    public Motorhome(int id, String brand, String model, int timesUsed, int kmDriven, int price, int typeId) {
        //will atleast be used when reading from repository
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.timesUsed = timesUsed;
        this.kmDriven = kmDriven;
        this.price = price;
        this.typeId = typeId;
        this.activeState = 0;
    }

    public Motorhome() {
        //used in readAll, then uses setters to set everything.
    }

    @Override
    public String toString() {
        return "Motorhome{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", timesUsed=" + timesUsed +
                ", kmDriven=" + kmDriven +
                ", price=" + price +
                ", activeState=" + activeState +
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

    public int getActiveState() {
        return activeState;
    }

    public void setActiveState(int activeState) {
        this.activeState = activeState;
    }
}
