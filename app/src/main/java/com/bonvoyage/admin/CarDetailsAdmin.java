package com.bonvoyage.admin;

import java.io.Serializable;

@SuppressWarnings("serial")

public class CarDetailsAdmin implements Serializable {

    public String carName;
    public String dropLocation;
    public String startDate;
    public String startTime;
    public String seatsAvailable;
    public String driverName;
    public String driverMobile;
    public String carNumber;
    public String orgName;

    public CarDetailsAdmin() {

    }

    public CarDetailsAdmin(String carName, String dropLocation,
                           String startDate, String startTime,
                           String seatsAvailable, String driverName,
                           String driverMobile, String carNumber,
                           String orgName) {
        this.carName = carName;
        this.dropLocation = dropLocation;
        this.startDate = startDate;
        this.startTime = startTime;
        this.seatsAvailable = seatsAvailable;
        this.driverName = driverName;
        this.driverMobile = driverMobile;
        this.carNumber = carNumber;
        this.orgName = orgName;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
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

    public String getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(String seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }


}