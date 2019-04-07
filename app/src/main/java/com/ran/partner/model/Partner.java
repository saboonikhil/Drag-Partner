package com.ran.partner.model;

import com.ran.partner.network.LoginResponse;

import java.io.Serializable;

public class Partner implements Serializable {

    private static Partner instance = null;
    private String _id;
    private String partnerName;
    private String partnerEmail;
    private String partnerContact;
    private Cab[] cabs;

    private Boolean res;
    private String response;
    private LoginResponse token;

    private Partner() {
    }

    public Partner(String partnerName, String partnerEmail, String partnerContact) {
        this.partnerName = partnerName;
        this.partnerEmail = partnerEmail;
        this.partnerContact = partnerContact;
    }

    public static Partner getInstance() {
        if (instance == null) {
            instance = new Partner();
        }
        return instance;
    }

    public String get_id() {
        return _id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public void setPartnerEmail(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }

    public String getPartnerContact() {
        return partnerContact;
    }

    public void setPartnerContact(String partnerContact) {
        this.partnerContact = partnerContact;
    }

    public Cab[] getCabs() {
        return cabs;
    }

    public void setCabs(Cab[] cabs) {
        this.cabs = cabs;
    }

    public Boolean res() {
        return res;
    }

    public String response() {
        return response;
    }

    public LoginResponse token() {
        return token;
    }
}