package com.ran.partner.network;

import com.ran.partner.model.Location;
import com.ran.partner.model.Partner;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EndPointInterface {

    @GET("/locations")
    Call<Location[]> listLocation();

    @POST("/signin")
    @FormUrlEncoded
    Call<Partner> authSignIn(
            @Field("email") String email,
            @Field("password") String password,
            @Field("role") String role
    );

    @GET("/api/admin/partners/{pID}")
    Call<Partner> partnerDetail(
            @Path("pID") String pID,
            @Query("x_key") String key,
            @Query("token") String token
    );

    @POST("/api/admin/signup")
    @FormUrlEncoded
    Call<Partner> addPartner(
            @Query("x_key") String key,
            @Query("token") String token,
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("alternateContact") String alternateContact,
            @Field("password") String password
    );

    @POST("/api/admin/{pID}/cabs")
    @FormUrlEncoded
    Call<Partner> addCab(
            @Path("pID") String pID,
            @Query("x_key") String key,
            @Query("token") String token,
            @Field("collegeName") String collegeName,
            @Field("pickup") String pickup,
            @Field("drop") String drop,
            @Field("startTime") String startTime,
            @Field("seats") String seats,
            @Field("fare") String fare,
            @Field("carName") String carName,
            @Field("carNumber") String carNumber
    );
}