package com.borisruzanov.popularmovies.model.interactor.list;

import android.util.Log;

import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.model.repository.list.ListRepository;
import com.borisruzanov.popularmovies.ui.list.ListCallback;

public class ListInteractor {

    ListRepository listRepository;

    public ListInteractor(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    public void sortByPopularity(ListCallback listCallback){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListInteractor - sortByPopularity");

        listRepository.sortByPopularity(listCallback);
    }

    public void sortByRating(ListCallback listCallback){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListInteractor - sortByRating");
        listRepository.sortByRating(listCallback);
    }

}
