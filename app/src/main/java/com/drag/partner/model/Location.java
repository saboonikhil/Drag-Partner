package com.drag.partner.model;

import java.io.Serializable;

public class Location implements Serializable {

    private String collegeName;
    private String[] setA;
    private String[] setB;

    public Location(String collegeName, String[] setA, String[] setB) {
        this.collegeName = collegeName;
        this.setA = setA;
        this.setB = setB;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String[] getSetA() {
        return setA;
    }

    public String[] getSetB() {
        return setB;
    }
}

