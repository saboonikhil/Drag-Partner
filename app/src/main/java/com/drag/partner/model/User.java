package com.drag.partner.model;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String contact;
    private String alternateContact;

    public User(String name, String contact, String alternateContact) {
        this.name = name;
        this.contact = contact;
        this.alternateContact = alternateContact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getAlternateContact() {
        return alternateContact;
    }
}