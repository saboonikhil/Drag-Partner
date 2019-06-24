package com.drag.partner.network;

public class APIUtils {

    private static final String BASE_URL = "http://ec2-52-66-214-151.ap-south-1.compute.amazonaws.com:8080";

    public static EndPointInterface getAPIService() {
        return RetrofitClientInstance.getRetrofitInstance(BASE_URL).create(EndPointInterface.class);
    }
}