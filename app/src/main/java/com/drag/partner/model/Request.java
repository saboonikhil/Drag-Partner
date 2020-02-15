package com.drag.partner.model;

import java.io.Serializable;

public class Request implements Serializable {

    private String _id;
    private String id;
    private boolean isActive;
    private String pickup;
    private String drop;
    private String startTime;
    private Cab cab;
    private Bid[] bids;
    private Partner allocatedPartner;

    public String get_id() {
        return _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public Bid[] getBids() {
        return bids;
    }

    public void setBids(Bid[] bids) {
        this.bids = bids;
    }

    public Partner getAllocatedPartner() {
        return allocatedPartner;
    }

    public void setAllocatedPartner(Partner allocatedPartner) {
        this.allocatedPartner = allocatedPartner;
    }
}