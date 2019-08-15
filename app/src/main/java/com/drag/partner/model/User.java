package com.drag.partner.model;

import java.io.Serializable;

public class User implements Serializable {

    private String _id;
    private String name;
    private String contact;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}