package com.nmh.project.models;

public class Customer {
    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private int id;
    private String cName;
    private int number;

    public Customer(int id, String cName, int number) {
        this.id = id;
        this.cName = cName;
        this.number = number;
    }

    public Customer(String cName, int number) {
        this.cName = cName;
        this.number = number;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", cName='" + cName + '\'' +
                ", number=" + number +
                '}';
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
