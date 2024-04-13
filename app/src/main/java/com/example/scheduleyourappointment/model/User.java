package com.example.scheduleyourappointment.model;

import java.util.ArrayList;

public class User {
    private String phone;
    private ArrayList<Appointment> appointments;


    public User(){}
    public User(String phone, ArrayList<Appointment> appointments) {
        this.phone = phone;
        this.appointments = appointments;
    }


    public String getPhone() {
        return phone;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }


    @Override
    public String toString() {
        return "User{" +
                "appointments=" + appointments +
                '}';
    }
}
