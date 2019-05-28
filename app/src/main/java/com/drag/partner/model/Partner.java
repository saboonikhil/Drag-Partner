package com.drag.partner.model;

import com.drag.partner.network.LoginResponse;

import java.io.Serializable;

public class Partner implements Serializable {

    private String _id;
    private String name;
    private String email;
    private String contact;
    private String alternateContact;
    private Cab[] cabs;

    private Boolean res;
    private String response;
    private LoginResponse token;

    public Partner(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getAlternateContact() {
        return alternateContact;
    }

    public void setAlternateContact(String alternateContact) {
        this.alternateContact = alternateContact;
    }
}