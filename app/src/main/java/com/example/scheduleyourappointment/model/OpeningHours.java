package com.example.scheduleyourappointment.model;

public class OpeningHours {

    private String startWeekHours;
    private String endWeekHours;
    private String startFridayHours;
    private String endFridayHours;


    public OpeningHours(){}
    public OpeningHours(String startWeekHours, String endWeekHours, String startFridayHours, String endFridayHours) {
        this.startWeekHours = startWeekHours;
        this.endWeekHours = endWeekHours;
        this.startFridayHours = startFridayHours;
        this.endFridayHours = endFridayHours;
    }


    public String getStartWeekHours() {
        return startWeekHours;
    }

    public void setStartWeekHours(String startWeekHours) {
        this.startWeekHours = startWeekHours;
    }

    public String getEndWeekHours() {
        return endWeekHours;
    }

    public void setEndWeekHours(String endWeekHours) {
        this.endWeekHours = endWeekHours;
    }

    public String getStartFridayHours() {
        return startFridayHours;
    }

    public void setStartFridayHours(String startFridayHours) {
        this.startFridayHours = startFridayHours;
    }

    public String getEndFridayHours() {
        return endFridayHours;
    }

    public void setEndFridayHours(String endFridayHours) {
        this.endFridayHours = endFridayHours;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                "startWeekHours='" + startWeekHours + '\'' +
                ", endWeekHours='" + endWeekHours + '\'' +
                ", startFridayHours='" + startFridayHours + '\'' +
                ", endFridayHours='" + endFridayHours + '\'' +
                '}';
    }
}
