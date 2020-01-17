package com.drag.partner.network;

import com.drag.partner.model.Cab;
import com.drag.partner.model.Location;
import com.drag.partner.model.Partner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EndPointInterface {

    @GET("/api/locations")
    Call<Location[]> authLocation(
            @Query("x_key") String key,
            @Query("token") String token,
            @Query("partnerVC") int versionCode
    );

    @POST("/signIn")
    @FormUrlEncoded
    Call<Partner> authSignIn(
            @Field("email") String email,
            @Field("password") String password,
            @Field("role") String role
    );

    @GET("/api/admin/partners/{pID}/trips")
    Call<Cab[]> partnerTrips(
            @Path("pID") String pID,
            @Query("x_key") String key,
            @Query("token") String token
    );

    @GET("/api/admin/rides")
    Call<List<Cab>> partnerRideList(
            @Query("x_key") String key,
            @Query("token") String token
    );

    @POST("/api/admin/signUp")
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
            @Field("city") String city,
            @Field("pickup") String pickup,
            @Field("drop") String drop,
            @Field("startTime") String startTime,
            @Field("seats") String seats,
            @Field("fare") String fare,
            @Field("carName") String carName
    );

    @POST("/api/rides")
    @FormUrlEncoded
    Call<Partner> addRide(
            @Query("x_key") String key,
            @Query("token") String token,
            @Field("pickup") String pickup,
            @Field("drop") String drop,
            @Field("startTime") String startTime,
            @Field("seats") String seats,
            @Field("fare") String fare
    );

    @PUT("/api/admin/cabs/{cID}")
    @FormUrlEncoded
    Call<Cab> cabUpdate(
            @Path("cID") String cID,
            @Query("x_key") String key,
            @Query("token") String token,
            @Field("carName") String carName,
            @Field("carNumber") String carNumber,
            @Field("driverName") String driverName,
            @Field("driverContact") String driverContact
    );
}