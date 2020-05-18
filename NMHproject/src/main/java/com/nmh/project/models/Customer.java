package com.nmh.project.models;

public class Customer {
    public int id;
    public String cName;
    public int number;

    public Customer(int id, String cName, int number) {
        this.id = id;
        this.cName = cName;
        this.number = number;
    }

    //----------------------------------------------------------------------------------------------------------------------------------
    // getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
