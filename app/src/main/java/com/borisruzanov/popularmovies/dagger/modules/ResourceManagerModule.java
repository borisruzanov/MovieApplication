package com.borisruzanov.popularmovies.dagger.modules;

import android.content.Context;
import android.util.Log;

import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.dagger.scopes.AppScope;
import com.borisruzanov.popularmovies.model.system.ResourceManager;

import dagger.Module;
import dagger.Provides;

@AppScope
@Module
public class ResourceManagerModule {

    Context context;

    public ResourceManagerModule(Context context) {
        this.context = context;
    }

    @AppScope
    @Provides
    public ResourceManager provideResourceManager(){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ResourceManagerModule - provideResourceManager");

        return new ResourceManager(context);
    }

}
