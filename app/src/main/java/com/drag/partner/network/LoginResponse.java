package com.drag.partner.network;

import com.drag.partner.model.Partner;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private String token;
    private String expires;
    private Partner dbObj;

    public String token() {
        return token;
    }

    public String expires() {
        return expires;
    }

    public Partner partner() {
        return dbObj;
    }
}