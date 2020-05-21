package com.nmh.project.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Motorhome {

    public int id;
    @NotEmpty
    public String brand;
    @NotEmpty
    public String model;
    @NotNull
    public int timesUsed;
    @NotNull
    public int kmDriven;
    public double extraPrice;
    public int activeState;
    @NotNull
    @Min(1)
    @Max(8)
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
        this.activeState = 0;
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

    public int getActiveState() {
        return activeState;
    }

    public void setActiveState(int activeState) {
        this.activeState = activeState;
    }
}
