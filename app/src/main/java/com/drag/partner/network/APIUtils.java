package com.drag.partner.network;

import android.content.Context;

public class APIUtils {

    public static EndPointInterface getAPIService(Context context) {
        return RetrofitClientInstance.getRetrofitInstance(context).create(EndPointInterface.class);
    }
}