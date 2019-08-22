package com.drag.partner.model;

import java.io.Serializable;

public class Cab implements Serializable {

    private String _id;
    private boolean isAvailable;
    private boolean isShared;
    private String type;
    private String pickup;
    private String drop;
    private String startTime;
    private String endTime;
    private String fare;
    private Rider[] riders;
    private String driverName;
    private String driverContact;
    private String carName;
    private String carNumber;

    public String getPickup() {
        return pickup;
    }

    public String getDrop() {
        return drop;
    }

    public String getStartTime() {
        return startTime;
    }

    public String get_id() {
        return _id;
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

    public String getType() {
        return type;
    }

    public Rider[] getRiders() {
        return riders;
    }

    public String getFare() {
        return fare;
    }
}