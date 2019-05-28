package com.drag.partner.model;

import java.io.Serializable;

public class Cab implements Serializable {

    private String _id;
    private boolean isAvailable;
    private boolean isBooked;
    private String collegeName;
    private String pickup;
    private String drop;
    private String startTime;
    private String endTime;
    private String seats;
    private String fare;
    private User[] riders;
    private String driverName;
    private String driverContact;
    private String carName;
    private String carNumber;

    public Cab(Boolean isAvailable, Boolean isBooked, String collegeName, String pickup, String drop, String startTime, String endTime,
               String seats, String carName, String carNumber, String fare, User[] riders, String driverName, String driverContact) {
        this.isAvailable = isAvailable;
        this.isBooked = isBooked;
        this.collegeName = collegeName;
        this.pickup = pickup;
        this.drop = drop;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seats = seats;
        this.carName = carName;
        this.carNumber = carNumber;
        this.riders = riders;
        this.driverName = driverName;
        this.fare = fare;
        this.driverContact = driverContact;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public User[] getRiders() {
        return riders;
    }

    public void setRiders(User[] riders) {
        this.riders = riders;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}