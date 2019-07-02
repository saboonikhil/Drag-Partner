package com.drag.partner.model;

import java.io.Serializable;

public class Cab implements Serializable {

    private String _id;
    private boolean isAvailable;
    private String tripId;
    private String city;
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

    public Cab(Boolean isAvailable, String city, String pickup, String drop, String startTime, String endTime,
               String seats, String carName, String carNumber, String fare, User[] riders, String driverName, String driverContact) {
        this.isAvailable = isAvailable;
        this.city = city;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}