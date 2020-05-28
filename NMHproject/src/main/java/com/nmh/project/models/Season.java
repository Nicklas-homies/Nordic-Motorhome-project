package com.nmh.project.models;

public class Season {
    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private int seasonId;
    private int startMonth;
    private int endMonth;
    private int seasonTypeId;


    public Season() {
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getSeasonTypeId() {
        return seasonTypeId;
    }

    public void setSeasonTypeId(int seasonTypeId) {
        this.seasonTypeId = seasonTypeId;
    }
}
