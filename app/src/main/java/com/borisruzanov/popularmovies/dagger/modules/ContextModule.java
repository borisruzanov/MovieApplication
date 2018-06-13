package com.borisruzanov.popularmovies.dagger.modules;

import android.content.Context;
import android.util.Log;

import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.dagger.scopes.AppScope;

import dagger.Module;
import dagger.Provides;

@AppScope
@Module
public class ContextModule {

    Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @AppScope
    @Provides
    public Context provideContext(){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ContextModule - provideContext");

        return  context;
    }

}
