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
}
