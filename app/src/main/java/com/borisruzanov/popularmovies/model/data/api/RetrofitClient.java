package com.borisruzanov.popularmovies.model.data.api;

import android.util.Log;

import com.borisruzanov.popularmovies.constants.Contract;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    /**
     * URLS
     **/
    private static final String BASE_URL = "http://api.themoviedb.org/";

    /**
     * Retrofit instance
     **/
    private static Retrofit getRetrofitInstance(){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "RetrofitClient - getRetrofitInstance");

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Api Service
     **/
    public static ApiService getApiService(){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "RetrofitClient - getApiService");
        return getRetrofitInstance().create(ApiService.class);
    }
}
