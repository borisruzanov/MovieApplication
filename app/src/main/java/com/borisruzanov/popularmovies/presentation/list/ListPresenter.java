package com.borisruzanov.popularmovies.presentation.list;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.popularmovies.MovieApplication;
import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.dagger.components.DaggerListComponent;
import com.borisruzanov.popularmovies.dagger.modules.ListModule;
import com.borisruzanov.popularmovies.entity.BasePojo;
import com.borisruzanov.popularmovies.model.interactor.list.ListInteractor;
import com.borisruzanov.popularmovies.ui.list.ListCallback;

import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class ListPresenter extends MvpPresenter<ListView> {

    @Inject ListInteractor listInteractor;

    public ListPresenter() {

        DaggerListComponent.builder()
                .appComponent(MovieApplication.component)
                .listModule(new ListModule())
                .build()
                .inject(this);
    }

    /**
     * Calling methods for HTTP Request to get data for list
     * Sending retrieved data to view methods for recycler
     */
    public void sortByPopularity() {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListPresenter - sortByPopularity");

        listInteractor.sortByPopularity(new ListCallback() {
            @Override
            public void setPhotosList(List<BasePojo.Result> photosList) {
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListPresenter - ListCallback - setPhotosList");
                getViewState().setData(photosList);
            }
        });
    }

    /**
     * Calling methods for HTTP Request to get data for list
     * Sending retrieved data to view methods for recycler
     */
    public void sortByRating() {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListPresenter - sortByRating");

        listInteractor.sortByRating(new ListCallback() {
            @Override
            public void setPhotosList(List<BasePojo.Result> photosList) {
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListPresenter - sortByRating - setPhotosList");

                getViewState().setData(photosList);
            }
        });
    }

}
