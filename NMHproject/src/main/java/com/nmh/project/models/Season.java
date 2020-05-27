package com.nmh.project.models;

public class Season {
    public int seasonId;
    public int startMonth;
    public int endMonth;
    public int seasonTypeId;

    public Season(int seasonId, int startMonth, int endMonth, int seasonTypeId) {
        this.seasonId = seasonId;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.seasonTypeId = seasonTypeId;
    }

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
