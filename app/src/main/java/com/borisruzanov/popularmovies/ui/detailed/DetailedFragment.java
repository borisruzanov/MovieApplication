package com.borisruzanov.popularmovies.ui.detailed;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.popularmovies.OnItemClickListener;
import com.borisruzanov.popularmovies.R;
import com.borisruzanov.popularmovies.model.data.api.RetrofitClient;
import com.borisruzanov.popularmovies.entity.ReviewModel;
import com.borisruzanov.popularmovies.entity.TrailerModel;
import com.borisruzanov.popularmovies.udacity.ProviderContract;
import com.borisruzanov.popularmovies.ui.favouriteList.FavouritesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.constants.FavouritesDbHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedFragment extends MvpAppCompatFragment {
    /**
     * General
     */
    View view;
    Toolbar toolbar;
    Button btnAddProvider;
    DetailedFragment detailedFragment;
    String path = "";
    private String stateValue = "detailed";

    /**
     * Reviews
     */
    RecyclerView recyclerReviews;
    List<ReviewModel.Result> reviewList;
    ReviewsAdapter reviewAdapter;

    /**
     * Trailer
     */
    RecyclerView recyclerTrailers;
    List<TrailerModel.Result> trailerList;
    TrailerAdapter trailerAdapter;

    /**
     * Database
     */
    private SQLiteDatabase mDb;


    public DetailedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - onCreateView");

        view = inflater.inflate(R.layout.fragment_detailed, container, false);

        btnAddProvider = view.findViewById(R.id.fr_detailed_btn_provider);
        btnAddProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovieInFavouritesByProvider();
            }
        });

        /**
         * Database
         */
        FavouritesDbHelper favouritesDbHelper = new FavouritesDbHelper(getActivity());
        mDb = favouritesDbHelper.getWritableDatabase();

        /**
         * Customize toolbar
         */
        detailedFragment = new DetailedFragment();
        toolbar = (Toolbar) view.findViewById(R.id.detailed_toolbar);
        toolbar.inflateMenu(R.menu.menu_detailed);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(getArguments().getString("title"));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Reviews Recycler
         */
        recyclerReviews = view.findViewById(R.id.reviewsRecyclerView);
        recyclerReviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewsAdapter(reviewList);
        recyclerReviews.setAdapter(reviewAdapter);

        /**
         * Trailer Recycler
         */
        recyclerTrailers = view.findViewById(R.id.trailersRecyclerView);
        recyclerTrailers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trailerList = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(trailerList, setOnItemClickCallback());
        recyclerTrailers.setAdapter(trailerAdapter);

        /**
         * Setting data
         */
        TextView tvTitle = view.findViewById(R.id.fr_detailed_tv_title);
        TextView tvPlot = view.findViewById(R.id.fr_detailed_tv_overview);
        TextView tvReleaseDate = view.findViewById(R.id.fr_detailed_tv_release_date);
        TextView tvVoteAverage = view.findViewById(R.id.fr_detailed_tv_vote_average);
        ImageView imgPoster = view.findViewById(R.id.fr_detailed_img_poster);
        tvTitle.setText(getArguments().getString("title"));
        tvPlot.setText(getArguments().getString("overview"));
        tvReleaseDate.setText(getArguments().getString("release_date"));
        tvVoteAverage.setText(getArguments().getString("vote_average"));
        Picasso.get()
                .load(getArguments().getString("poster_path"))
                .into(imgPoster);

        if (savedInstanceState != null) {
            stateValue = String.valueOf(savedInstanceState.getInt(Contract.STATE_KEYS));
            // Do something with value if needed
        }

        /**
         * Getting data for Reviews and Trailers
         */
        getReviewsData();
        getTrailersData();

        return view;
    }

    /**
     * Put movie in DB favourite
     */
    private void addMovieInFavouritesByProvider() {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - addMovieInFavouritesByProvider");

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.TableInfo.COLUMN_ID, getArguments().getString("id"));
        contentValues.put(Contract.TableInfo.COLUMN_TITLE, getArguments().getString("title"));
        contentValues.put(Contract.TableInfo.COLUMN_POSTER_PATH, getArguments().getString("poster_path"));
        contentValues.put(Contract.TableInfo.COLUMN_RELEASE_DATE, getArguments().getString("release_date"));
        contentValues.put(Contract.TableInfo.COLUMN_RATING, getArguments().getString("vote_average"));
        contentValues.put(Contract.TableInfo.COLUMN_OVERVIEW, getArguments().getString("overview"));
        Uri uri = getActivity().getContentResolver().insert(ProviderContract.TableEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "URI IS --- " + uri.toString());
        }
    }

    /**
     * Getting data for reviews
     */
    public void getReviewsData() {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - getReviewsData");
        RetrofitClient
                .getApiService()
                .loadReviews(getArguments().getString("id"), getString(R.string.api_key))
                .enqueue(new Callback<ReviewModel>() {
                    @Override
                    public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - getReviewsData - onResponse");
                        ReviewModel reviewModel = response.body();
                        Log.d("tagReview", "Current response " + String.valueOf(reviewModel.getPage()));
                        reviewList.addAll(reviewModel.getResults());
                        recyclerReviews.getAdapter().notifyDataSetChanged();
                        for (ReviewModel.Result result : reviewModel.getResults()) {
                            Log.d("tag", "Movie list111 " + result.getContent());
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewModel> call, Throwable t) {
                        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - getReviewsData - onFailure");
                    }
                });
    }

    /**
     * Getting data for trailers
     */
    public void getTrailersData() {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - getTrailersData");
        RetrofitClient.
                getApiService()
                .loadTrailers(getArguments().getString("id"), getString(R.string.api_key))
                .enqueue(new Callback<TrailerModel>() {
                    @Override
                    public void onResponse(Call<TrailerModel> call, Response<TrailerModel> response) {
                        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - getTrailersData - onResponse");

                        TrailerModel trailerModel = response.body();
                        if (trailerModel != null) {
                            trailerList.addAll(trailerModel.getResults());
                            recyclerTrailers.getAdapter().notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<TrailerModel> call, Throwable t) {
                        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - getTrailersData - onFailure");

                    }
                });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - onSaveInstanceState");
        outState.putString(Contract.STATE_KEYS, stateValue);
    }

    private Fragment previousFragment(String path){
        Fragment fragment = new Fragment();
        switch (path) {
            case "sort":
                fragment = new com.borisruzanov.popularmovies.ui.list.ListFragment().getInstance("sort");
                break;
            case "favourite":
                fragment = new FavouritesFragment().getInstance("favourite");
                break;
            case "popular":
                fragment = new com.borisruzanov.popularmovies.ui.list.ListFragment().getInstance("popular");
                break;
        }
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - onOptionsItemSelected");
        path = getArguments().getString("path");
        if (item.getItemId() == android.R.id.home) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
            fm.beginTransaction().replace(R.id.main_frame_list, previousFragment(path)).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "DetailedFragment - setOnItemClickCallback - onItemClicked");
                String url = "https://www.youtube.com/watch?v=".concat(trailerList.get(position).getKey());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

        };
        return onItemClickCallback;
    }
}
