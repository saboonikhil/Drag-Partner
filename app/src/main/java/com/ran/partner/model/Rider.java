package com.ran.partner.model;

import java.io.Serializable;

public class Rider implements Serializable {

    private String _id;
    private String name;
    private String contact;

    public Rider(String id, String name, String contact) {
        _id = id;
        this.name = name;
        this.contact = contact;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}