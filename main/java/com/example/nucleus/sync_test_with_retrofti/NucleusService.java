package com.example.nucleus.sync_test_with_retrofti;


import java.util.List;

import modelo.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NucleusService {


    public static final String BASE_URL = "https://api-nucleus-user.herokuapp.com/api/";

    @GET("user/")
    Call<List<User>> getListaUser();

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String id);
}
