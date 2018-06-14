package com.borisruzanov.popularmovies.ui.favouriteList;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.popularmovies.OnItemClickListener;
import com.borisruzanov.popularmovies.R;
import com.borisruzanov.popularmovies.ui.detailed.DetailedFragment;
import com.borisruzanov.popularmovies.constants.Contract;
import com.borisruzanov.popularmovies.constants.FavouritesDbHelper;
import com.borisruzanov.popularmovies.entity.FavouriteModel;
import com.borisruzanov.popularmovies.udacity.ProviderContract;
import com.borisruzanov.popularmovies.ui.list.ListFragment;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends MvpAppCompatFragment implements FavouritesAdapter.ItemClickListener {

    /**
     * General
     */
    View view;
    Toolbar toolbar;

    RecyclerView recyclerView;
    ArrayList<FavouriteModel> favouritesList;
    FavouritesAdapter favouritesAdapter;
    ListFragment listFragment;

    String id ;
    String posterPath;
    String title;
    String releaseDate;
    String vote;
    String overview;

    /**
     * DB
     */
    FavouritesDbHelper favouritesDbHelper;
    Cursor cursor;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Helping with showing needed list depends on got parameter
     * @param path - sort / popular / favourite
     * @return - chosen fragment with chosen path
     */
    //TODO Вынести в общий метод
    public FavouritesFragment getInstance(String path) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "FavouritesFragment - onCreateView");

        view = inflater.inflate(R.layout.fragment_favourites, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.main_favourite_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);
        listFragment = new ListFragment();
        favouritesList = new ArrayList<>();


        /**
         * DB
         */
        favouritesDbHelper = new FavouritesDbHelper(getActivity());
        cursor = getDataForListFromContentProvider();

        if(savedInstanceState != null) {
            Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "FavouritesFragment - savedInstanceState != null");

            id = savedInstanceState.getString("id");
            Log.d("TAG_FRAGMENT", "mID - " + id);

            posterPath = savedInstanceState.getString("poster_path");
            Log.d("TAG_FRAGMENT", "mPOSTER PATH - " + posterPath);

            title = savedInstanceState.getString("title");
            Log.d("TAG_FRAGMENT", "mTITLE - " + title);

            releaseDate = savedInstanceState.getString("release_date");
            Log.d("TAG_FRAGMENT", "mRELEASE DATE - " + releaseDate);

            vote = savedInstanceState.getString("rating");
            Log.d("TAG_FRAGMENT", "mVOTE - " + vote);

            overview = savedInstanceState.getString("overview");
            Log.d("TAG_FRAGMENT", "mOVERVIEW - " + overview);

            favouritesList.add(new FavouriteModel(id, posterPath, title, releaseDate, vote, overview));
        }

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            Log.d("TAG_FRAGMENT", "ID - " + id);

            posterPath = cursor.getString(cursor.getColumnIndex("poster_path"));
            Log.d("TAG_FRAGMENT", "POSTER PATH - " + posterPath);

            title = cursor.getString(cursor.getColumnIndex("title"));
            Log.d("TAG_FRAGMENT", "TITLE - " + title);

            releaseDate = cursor.getString(cursor.getColumnIndex("release_date"));
            Log.d("TAG_FRAGMENT", "RELEASE DATE - " + releaseDate);

            vote = cursor.getString(cursor.getColumnIndex("rating"));
            Log.d("TAG_FRAGMENT", "VOTE - " + vote);

            overview = cursor.getString(cursor.getColumnIndex("overview"));
            Log.d("TAG_FRAGMENT", "OVERVIEW - " + overview);

            favouritesList.add(new FavouriteModel(id, posterPath, title, releaseDate, vote, overview));
        }
        cursor.close();

        favouritesAdapter = new FavouritesAdapter(favouritesList, setOnItemClickCallback());
        recyclerView = view.findViewById(R.id.recycler_favourites);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(favouritesAdapter);
        return view;
    }

    /**
     * DB
     */
    public Cursor getDataForListFromContentProvider() {
        try {
            return getContext().getContentResolver().query(ProviderContract.TableEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            Log.e("tag", "Failed while loading data");
        }
        return null;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "FavouritesFragment - onSaveInstanceState");
        outState.putString(Contract.STATE_KEY, "favourite");
        outState.putString("id", id);
        outState.putString("title", title);
        outState.putString("release_date", releaseDate);
        outState.putString("rating", vote);
        outState.putString("overview", overview);
        Log.d(Contract.TAG_STATES_CHECKING, "Bundle is " + outState.toString());
    }


    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "FavouritesFragment - setOnItemClickCallback - onItemClicked");
                Bundle bundle = new Bundle();
                bundle.putString("title", favouritesList.get(position).getTitle());
                bundle.putString("overview", favouritesList.get(position).getOverview());
                bundle.putString("release_date", favouritesList.get(position).getRelease_date());
                bundle.putString("path", "favourite");
                bundle.putString("poster_path", favouritesList.get(position).getPoster_path());
                bundle.putString("id", favouritesList.get(position).getId());

                DetailedFragment detailedFragment = new DetailedFragment();
                detailedFragment.setArguments(bundle);

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_frame_list, detailedFragment);
                transaction.commit();
            }

        };
        return onItemClickCallback;
    }

    @Override
    public void onItemClick(View view, int position) {
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "FavouritesFragment - onCreateOptionsMenu");

        super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
        toolbar.inflateMenu(R.menu.menu_main);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "FavouritesFragment - onOptionsItemSelected");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.menu_sort:
                transaction.replace(R.id.main_frame_list, listFragment.getInstance("sort"), "list_fragment_tag");
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_popular:
                transaction.replace(R.id.main_frame_list, listFragment.getInstance("popular"), "list_fragment_tag");
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
