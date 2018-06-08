package com.bonvoyage.admin;

/**
 * Created by nikhil on 8/6/18.
 */
public class Driver {

    public String driverId;
    public String driverName;
    public String driverNumber;
    public String orgName;

    public Driver() {

    }

    public Driver(String driverId, String driverName, String driverNumber, String orgName) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.orgName = orgName;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}

