package com.borisruzanov.popularmovies.model.repository.list;

import android.util.Log;

import com.borisruzanov.popularmovies.R;
import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.entity.BasePojo;
import com.borisruzanov.popularmovies.model.data.api.ApiService;
import com.borisruzanov.popularmovies.model.system.ResourceManager;
import com.borisruzanov.popularmovies.ui.list.ListCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRepository {

    ApiService apiService;
    ResourceManager resourceManager;

    public ListRepository(ApiService apiService, ResourceManager resourceManager) {
        this.apiService = apiService;
        this.resourceManager = resourceManager;
    }

    /**
     *Getting data from HTTP Request
     * Calling listCallBack which is implemented in presenter
     * @param listCallback
     */
    public void sortByPopularity(ListCallback listCallback) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListRepository - sortByPopularity");

        apiService.getPopularList(resourceManager.getString(R.string.api_key)).enqueue(new Callback<BasePojo>() {
            @Override
            public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                /*BasePojo basePojo = response.body();
                listCallback.setPhotosList(basePojo.getResults());*/
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListRepository - sortByPopularity - onResponse");

                fetchList(listCallback, response.body().getResults());
            }

            @Override
            public void onFailure(Call<BasePojo> call, Throwable t) {
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListRepository - sortByPopularity - onFailure");
                Log.d("tag", "Response failed" + t.toString());

            }
        });
    }

    public void sortByRating(ListCallback listCallback) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListRepository - sortByRating");

        apiService.getPhotosList(resourceManager.getString(R.string.api_key)).enqueue(new Callback<BasePojo>() {
            @Override
            public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                /*BasePojo basePojo = response.body();
                listCallback.setPhotosList(basePojo.getResults());*/
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListRepository - sortByRating - onResponse");

                fetchList(listCallback, response.body().getResults());
            }

            @Override
            public void onFailure(Call<BasePojo> call, Throwable t) {
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListRepository - sortByRating - onFailure");

                Log.d("tag", "Response failed" + t.toString());

            }
        });
    }

    private void fetchList(ListCallback listCallback, List<BasePojo.Result> photosList){
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "ListRepository - fetchList");

        listCallback.setPhotosList(photosList);
    }

}
