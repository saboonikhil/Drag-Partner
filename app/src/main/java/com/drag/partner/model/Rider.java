package com.drag.partner.model;

import java.io.Serializable;

public class Rider implements Serializable {

    private User _id;
    private String tripId;
    private String tripStatus;
    private String pickup;
    private String drop;
    private String seats;
    private String fare;
    private String luggageCount;

    public String getTripId() {
        return tripId;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public String getFare() {
        return fare;
    }

    public String getSeats() {
        return seats;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDrop() {
        return drop;
    }

    public User get_id() {
        return _id;
    }

    public String getLuggageCount() {
        return luggageCount;
    }
}