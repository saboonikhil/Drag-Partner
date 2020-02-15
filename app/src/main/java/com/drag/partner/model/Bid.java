package com.drag.partner.model;

import java.io.Serializable;

public class Bid implements Serializable {

    private Partner partner;
    private String price;

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}