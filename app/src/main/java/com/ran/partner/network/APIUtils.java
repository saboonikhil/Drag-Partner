package com.ran.partner.network;

public class APIUtils {

    private static final String BASE_URL = "http://ec2-54-184-16-106.us-west-2.compute.amazonaws.com:8080";

    private APIUtils() {
    }

    public static EndPointInterface getAPIService() {
        return RetrofitClientInstance.getRetrofitInstance(BASE_URL).create(EndPointInterface.class);
    }
}