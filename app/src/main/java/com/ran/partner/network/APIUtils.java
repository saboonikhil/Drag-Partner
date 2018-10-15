package com.ran.partner.network;

public class APIUtils {

    private static final String BASE_URL = "http://1d5a413f.ngrok.io";

    private APIUtils() {
    }

    public static EndPointInterface getAPIService() {
        return RetrofitClientInstance.getRetrofitInstance(BASE_URL).create(EndPointInterface.class);
    }
}