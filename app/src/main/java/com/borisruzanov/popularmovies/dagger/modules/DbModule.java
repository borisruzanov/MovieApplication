package com.borisruzanov.popularmovies.dagger.modules;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.constants.FavouritesDbHelper;
import com.borisruzanov.popularmovies.dagger.scopes.AppScope;
import com.borisruzanov.popularmovies.udacity.ProviderContract;

import dagger.Module;
import dagger.Provides;

@AppScope
@Module
public class DbModule {

    Context context;

    public DbModule(Context context) {
        this.context = context;
    }

    @AppScope
    @Provides
    public FavouritesDbHelper provideFavoritesDbHelper() {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DbModule - provideFavoritesDbHelper");

        return new FavouritesDbHelper(context);
    }

}
