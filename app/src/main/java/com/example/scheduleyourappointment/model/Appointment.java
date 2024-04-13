package com.example.scheduleyourappointment.model;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.TimeZone;

public class Appointment {
    private String id;
    private String date;
    private String time;


    public Appointment(){}

    public Appointment(String date, String time, String id) {
        this.date = date;
        this.time = time;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' ;
    }

}
