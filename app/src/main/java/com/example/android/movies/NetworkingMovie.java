package com.example.android.movies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdulrhman on 20/10/2016.
 */
public final class NetworkingMovie {
   static String LOG_TAG = NetworkingMovie.class.getSimpleName()+ " WRONG";
    static String LOG_TAG_CORRECT = NetworkingMovie.class.getSimpleName()+ "MYLOG-CORRECT";

    public static   URL createUrl(String url) {
        URL createdUrl = null;
        try {
            createdUrl = new URL(url);
            Log.d(LOG_TAG_CORRECT," success to create the url");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(LOG_TAG,"faild to create url");

        }

        return createdUrl;
    }

    public static String MakeHttpRequest(URL url) throws IOException {

        HttpURLConnection connection = null;
        InputStream stream = null;
        String JsonResponse = "";

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                stream = connection.getInputStream();
                JsonResponse = ReadFromStream(stream);
                Log.d(LOG_TAG_CORRECT," success to make http request  ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG," failed to make http request");
        } finally {
            if (connection != null) {
                connection.disconnect();
                Log.d(LOG_TAG_CORRECT," finally called , connection closed");
            }
            if (stream != null) {
                stream.close();
                Log.d(LOG_TAG_CORRECT,"finally called stream closed  ");
            }
        }

        return JsonResponse;
    }


    public static String ReadFromStream(InputStream stream) {

        StringBuilder jsonResponse = new StringBuilder();

        try {
            if (stream != null) {
                InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
                BufferedReader bufferReader = new BufferedReader(streamReader);

                String line = bufferReader.readLine();
                while (line != null) {
                    jsonResponse.append(line);
                    line = bufferReader.readLine();
                }
                Log.d(LOG_TAG_CORRECT," success for readFromStream");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG,"failed to readStream data from the API");
        }
        return jsonResponse.toString();
    }


    public static List<ItemsClass> ParseData(String jsonresponse) {
        if (TextUtils.isEmpty(jsonresponse)) {
            return null;
        }
        List<ItemsClass> movieArrayList = new ArrayList<ItemsClass>();

        try {
            JSONObject rootObject = new JSONObject(jsonresponse);
            JSONArray resultsArray = rootObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                StringBuilder posterbuilder = new StringBuilder();
                posterbuilder.append("http://image.tmdb.org/t/p/w185");
                posterbuilder.append(resultObject.getString("poster_path"));
                String poster = posterbuilder.toString();
                String title = resultObject.getString("title");
                String overview = resultObject.getString("overview");
                int id = resultObject.getInt("id");
                double rating = resultObject.getDouble("vote_average");
                String releaseDate = resultObject.getString("release_date");
                Log.d(LOG_TAG_CORRECT," success to call parseData");
                ItemsClass itemsClassObject = new ItemsClass(poster, title, releaseDate, overview, rating,id);
                movieArrayList.add(itemsClassObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG,"failed to parse the data correctly ");
        }

        Log.d(LOG_TAG,movieArrayList.toString()+" movies");
        return movieArrayList;
    }
    public static List<ItemsClass> FetchData(String RequestedURL) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(LOG_TAG,"failed to sleep the thread after 2000 seconds");
        }

        URL theUrl = createUrl(RequestedURL);
        String jsonResponse = null;


        try {

            jsonResponse = MakeHttpRequest(theUrl);

            Log.d(LOG_TAG_CORRECT,"Fetch data called http request ");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG," failed to fetch the data proberly ");
        }

        List<ItemsClass> FinalMovieArrayList = ParseData(jsonResponse);
        return FinalMovieArrayList;

    }

}
