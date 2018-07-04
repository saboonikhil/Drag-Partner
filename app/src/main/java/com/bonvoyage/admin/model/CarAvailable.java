package com.bonvoyage.admin.model;

import java.io.Serializable;

@SuppressWarnings("serial")

public class CarAvailable implements Serializable {

    private String collegeName;
    private String carName;
    private String pickupLocation;
    private String dropLocation;
    private String startDate;
    private String startTime;
    private String seats;
    private String driverId;
    private String driverName;
    private String driverNumber;
    private String carNumber;
    private String fare;
    private boolean shared;

    public CarAvailable() {

    }

    public CarAvailable(String collegeName, String carName, String pickupLocation, String dropLocation, String startDate,
                     String startTime, String seats, String driverId, String driverName, String driverNumber,
                     String carNumber, String fare) {
        this.collegeName = collegeName;
        this.carName = carName;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.startDate = startDate;
        this.startTime = startTime;
        this.seats = seats;
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.carNumber = carNumber;
        this.fare = fare;
        this.shared = false;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean isShared) {
        shared = isShared;
    }

}
