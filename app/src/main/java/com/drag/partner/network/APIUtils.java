package com.drag.partner.network;

public class APIUtils {

    private static final String BASE_URL = "https://a3cc5de8.ngrok.io";

    public static EndPointInterface getAPIService() {
        return RetrofitClientInstance.getRetrofitInstance(BASE_URL).create(EndPointInterface.class);
    }
}