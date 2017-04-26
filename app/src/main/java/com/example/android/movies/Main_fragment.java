package com.example.android.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main_fragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ItemsClass>>, SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    adapterMovie adapter;
    View progressBar;
    TextView emptyText;
    ImageView emptyView;
    GridView movieGrid;
    int LOADER_ID = 1;

    handlerCallback callbackInterface;
    public static boolean favClicked = false;
    ItemsClass currentItem;


    public void setCurrentItem(handlerCallback callback) {
        callbackInterface = callback;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.main_fragment, container, false);
        SharedPreferences mypreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mypreferences.registerOnSharedPreferenceChangeListener(this);
        adapter = new adapterMovie(getActivity(), new ArrayList<ItemsClass>(),callbackInterface);
        progressBar = rootview.findViewById(R.id.loadingBar);
        emptyText = (TextView) rootview.findViewById(R.id.emptytext);
        movieGrid = (GridView) rootview.findViewById(R.id.gridview);
        emptyView = (ImageView) rootview.findViewById(R.id.emptyimage);

     if (isConnected()) {
            movieGrid.setAdapter(adapter);
            movieGrid.setEmptyView(emptyText);
            movieGrid.setEmptyView(emptyView);

            movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    currentItem = (ItemsClass) adapter.getItem(i);
                    callbackInterface.onItemSelected(currentItem);
                    Log.d("DEDE", currentItem.toString());
                }
            });
        } else {


            movieGrid.setAdapter(adapter);


            movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    currentItem = (ItemsClass) adapter.getItem(i);
                    callbackInterface.onItemSelected(currentItem);
                    Log.d("DEDE", currentItem.toString());

                }
            });
            progressBar.setVisibility(View.GONE);
         emptyView.setVisibility(View.GONE);

        }

        LoaderManager loaderManager = getLoaderManager();
        Log.d("DEBUG", " the  InitLoader  method is called");

        loaderManager.initLoader(LOADER_ID, null, this);


        return rootview;
    }




    @Override
    public android.support.v4.content.Loader<List<ItemsClass>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = preferences.getString(getString(R.string.sort_key), getString(R.string.defaultSort));

        Uri urlRequest = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder uriBuilder = urlRequest.buildUpon();
        uriBuilder.appendPath(sortBy);
        uriBuilder.appendQueryParameter("api_key", "5f7c26a298341681f7a738b181585c63");
        String poplarurl = "http://api.themoviedb.org/3/movie/popular?api_key=5f7c26a298341681f7a738b181585c63";
        String topratedurl = "http://api.themoviedb.org/3/movie/top_rated?api_key=5f7c26a298341681f7a738b181585c63";
        if (uriBuilder.toString().equals(poplarurl) || uriBuilder.toString().equals(topratedurl)) {
            return new LoaderMovie(getActivity(), uriBuilder.toString());
        } else {
            if (OFFLINELIST() != null) {
                return new LoaderMovie(getActivity());
            } else {
                emptyText.setText("you have empty list");

            }
            return new LoaderMovie(getActivity());
        }
    }

    @Override
    public void onLoadFinished(Loader<List<ItemsClass>> loader, List<ItemsClass> itemsClasses) {

        if (adapter != null) {

            if (!adapter.isEmpty()) {
                adapter.clear();
            }

            if (itemsClasses != null && !itemsClasses.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                adapter.addAll(itemsClasses);
            } else {

                emptyText.setText("no movies found dude,\nchange your preference");
                emptyView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public void onLoaderReset(Loader<List<ItemsClass>> loader) {
        if (adapter != null) {
            adapter.clear();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent settingIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(settingIntent);
                break;

        }
        return true;
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (isAdded()) {
            if (key.equals(getString(R.string.sort_key)) && adapter != null) {
                adapter.clear();
                emptyText.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            } else {
                progressBar.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);

                Toast toast = Toast.makeText(getContext(), "added first favorite movie", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, Gravity.CENTER, 0);
                toast.show();
            }
        }
    }

    public List<ItemsClass> OFFLINELIST() {


        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(getContext());

        String storedJsonList = mypreference.getString("storedjsonList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemsClass>>() {
        }.getType();

        List<ItemsClass> movieArrayList = new ArrayList<ItemsClass>();
        movieArrayList = gson.fromJson(storedJsonList, type);
        if (movieArrayList != null) {
            movieArrayList.size();
            return movieArrayList;
        } else
            return null;

    }

    public int checkOFFLINE() {
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        String storedJsonList = mypreference.getString("storedjsonList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemsClass>>() {
        }.getType();

        List<ItemsClass> movieArrayList = new ArrayList<ItemsClass>();
        movieArrayList = gson.fromJson(storedJsonList, type);

        if (movieArrayList != null)
            return movieArrayList.size();
        else
            return 0;

    }


}