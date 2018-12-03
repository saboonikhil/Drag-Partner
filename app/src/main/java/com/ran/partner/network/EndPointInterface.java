package com.ran.partner.network;

import com.ran.partner.model.Cab;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EndPointInterface {

    @POST("/api/admin/cabs")
    @FormUrlEncoded
    Call<Cab> addCab(@Query("x_key") String key,
                     @Query("token") String token,
                     @Field("collegeName") String collegeName,
                     @Field("pickup") String pickup,
                     @Field("drop") String drop,
                     @Field("startTime") String startTime,
                     @Field("seats") String seats,
                     @Field("fare") String fare,
                     @Field("carName") String carName,
                     @Field("startTime") String carNumber);
}