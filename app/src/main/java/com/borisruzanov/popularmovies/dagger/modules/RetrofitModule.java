package com.borisruzanov.popularmovies.dagger.modules;

import android.util.Log;

import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.dagger.scopes.AppScope;
import com.borisruzanov.popularmovies.model.data.api.ApiService;
import com.borisruzanov.popularmovies.model.data.api.RetrofitClient;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@AppScope
@Module
public class RetrofitModule {

    private final String BASE_URL = "http://api.themoviedb.org/";

    @AppScope
    @Provides
    public ApiService provideApiService(){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "RetrofitModule - provideApiService");

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

}
