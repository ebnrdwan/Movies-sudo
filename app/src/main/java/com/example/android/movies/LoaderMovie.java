package com.example.android.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdulrhman on 20/10/2016.
 */
public class LoaderMovie extends AsyncTaskLoader<List<ItemsClass>> {
    String url;
    int id = Detail_fragment.id;


    public LoaderMovie(Context context, String url) {

        super(context);
        this.url = url;
    }
    public LoaderMovie(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
        Log.d("LOG_TAG_CORRECT", " onStartLoading called");
    }

    @Override
    public List<ItemsClass> loadInBackground() {
        String title;
        String overview;
        String releaseDate;
        double rate;
        String storedPoster;
        boolean favoClicked = MainActivity.favClicked;
        List<ItemsClass> movieList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = preferences.getString("sort_by","-1");

        if (sortBy.equals("myfavorite")) {
            movieList = OFFLINELIST();
            return movieList;
        }
        else if (!isConnected()){
            movieList = OFFLINELIST();
            return movieList;
        }

       else if (isConnected()) {
            String poplarurl="http://api.themoviedb.org/3/movie/popular?api_key=5f7c26a298341681f7a738b181585c63";
            String topratedurl="http://api.themoviedb.org/3/movie/top_rated?api_key=5f7c26a298341681f7a738b181585c63";
            if (url ==null) {
                return null;
            }
            movieList = NetworkingMovie.FetchData(url);

            return movieList;
        }

        return movieList;
    }

    public boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public List<ItemsClass> OFFLINELIST() {



        SharedPreferences mypreference= PreferenceManager.getDefaultSharedPreferences(getContext());

        String storedJsonList = mypreference.getString("storedjsonList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemsClass>>() {
        }.getType();

        List<ItemsClass> movieArrayList = new ArrayList<ItemsClass>();
        movieArrayList = gson.fromJson(storedJsonList, type);

        return movieArrayList;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }



}





