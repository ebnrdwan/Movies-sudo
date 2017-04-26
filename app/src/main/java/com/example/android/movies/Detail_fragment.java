package com.example.android.movies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
//https://api.themoviedb.org/3/movie/284052/videos?api_key=5f7c26a298341681f7a738b181585c63&language=en-US
//        https://api.themoviedb.org/3/movie/284052/recommendations?api_key=5f7c26a298341681f7a738b181585c63&language=en-US&page=1
//        https://api.themoviedb.org/3/movie/284052/reviews?api_key=5f7c26a298341681f7a738b181585c63&language=en-US
//https://www.youtube.com/watch?v=EZ-zFwuR0FY
//http://img.youtube.com/vi/EZ-zFwuR0FY/default.jpg
public class Detail_fragment extends Fragment {

    private Bitmap theposterImage;
    private String theposter;
    String StoredPoster;
    private String thetitle;
    private String theoverView;
    private String thereleaseDate;
    private double therating;
    public static int id;
    public String videoKey;
    public String videoTitle;
    public static ArrayList<ItemTrailer> itemTrailerList;
    adapterTrailer adapterTrailer;
    ListView trailersList;
    adapterReview adapterReview;
    ListView reviewList;
    ArrayList<ItemReview> itemReviewsList;
    boolean ischecked = true;
    ArrayList<ItemsClass> serItems;
    SharedPreferences mypreference;
    final boolean[] va = {true};
    ImageButton favo;


    private String api_key = "5f7c26a298341681f7a738b181585c63";

    public static String Base_Detail_url = "http://api.themoviedb.org/3/movie";
    public String full_url;

    //


    public Detail_fragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        AndroidNetworking.initialize(getContext());
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        Bundle getdatabundle = getArguments();
        ItemsClass getdata = getdatabundle.getParcelable("ItemObject");
        if (getdata != null) {
            ItemsClass parelableItem = getdata;
            thetitle = parelableItem.getTitle();
            theoverView = parelableItem.getOverview();
            thereleaseDate = parelableItem.getReleaseDate();
            therating = parelableItem.getrating() / 2;
            theposter = parelableItem.getPoster();
            id = parelableItem.getId();
        } else {


            return null;
        }

        //// TODO: 21/11/2016 list the reviews
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/" + Integer.toString(id) + "/reviews?api_key=5f7c26a298341681f7a738b181585c63").build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                itemReviewsList = new ArrayList<>();
                if (getActivity() != null)
                    adapterReview = new adapterReview(getContext(), itemReviewsList);
                itemReviewsList = ParseReview(response);
                if (adapterReview != null) adapterReview.notifyDataSetChanged();
                reviewList.setAdapter(adapterReview);


//                Toast.makeText(getContext(), "successfully load the reviews" + itemReviewsList.size(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(ANError anError) {
            }
        });


        //// TODO: 21/11/2016 Get Trailers
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/" + Integer.toString(id) + "/videos?api_key=5f7c26a298341681f7a738b181585c63").build().getAsJSONObject(new JSONObjectRequestListener() {

            @Override
            public void onResponse(JSONObject response) {
                itemTrailerList = new ArrayList<ItemTrailer>();
                if (getActivity() != null)
                    adapterTrailer = new adapterTrailer(getActivity(), itemTrailerList);
                itemTrailerList = ParseVideos(response);
                itemTrailerList.size();
                if (adapterTrailer != null)
                    adapterTrailer.notifyDataSetChanged();
                trailersList.setAdapter(adapterTrailer);
//                Toast.makeText(getContext(), "successfully Fetched the video data" + itemTrailerList.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                Log.d("ERER", "can't load the trailer");
//                Toast.makeText(getContext(), "can't load the trailer", Toast.LENGTH_SHORT).show();
            }
        });
        final ImageView poster = (ImageView) rootView.findViewById(R.id.posterdetail);
        theposterImage = decodeBase64(theposter);
        poster.setImageBitmap(theposterImage);
        Picasso.with(getContext()).load(theposter).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                theposterImage = bitmap;
                poster.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        if (theposterImage != null)
            StoredPoster = encodeTobase64(theposterImage);

        favo = (ImageButton) rootView.findViewById(R.id.favorite);
        final boolean[] va = {true};
        serItems = new ArrayList<ItemsClass>();
        favo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                onClicked();

//
            }

        });

        TextView title = (TextView) rootView.findViewById(R.id.movietitle);
        title.setText(thetitle);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        releaseDate.setText(thereleaseDate);
        RatingBar ratingbar = (RatingBar) rootView.findViewById(R.id.ratingmovie);
        ratingbar.setRating((float) therating);
        TextView overivew = (TextView) rootView.findViewById(R.id.overivewmovie);
        overivew.setText(theoverView);

        reviewList = (ListView) rootView.findViewById(R.id.reviewList);
        trailersList = (ListView) rootView.findViewById(R.id.movieList);
        trailersList.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                            {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                    ItemTrailer current = (ItemTrailer) adapterTrailer.getItem(i);
                                                    String key = current.getKey();
                                                    String videoUrl = "https://www.youtube.com/watch?v=" + key;
                                                    Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                                                    videoIntent.setData(Uri.parse(videoUrl));
                                                    getContext().startActivity(videoIntent);
                                                }
                                            }

        );

        return rootView;
    }


    // TODO:  helper methods
    public ArrayList<ItemTrailer> ParseVideos(JSONObject object) {
        try {
            JSONArray results = object.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject vidoes = results.getJSONObject(i);
                if (vidoes.has("name") || vidoes.has("key")) {
                    String title = vidoes.getString("name");
                    String key = vidoes.getString("key");
                    videoKey = key;
                    videoTitle = title;
                }
                itemTrailerList.add(new ItemTrailer(videoTitle, videoKey));
            }
//            Toast.makeText(getContext(), "Successfully parsed Video Data" + itemTrailerList.size(), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
//            Toast.makeText(getContext(), "can't parse the video data", Toast.LENGTH_SHORT).show();

        } finally {

            return itemTrailerList;
        }
    }

    public ArrayList<ItemReview> ParseReview(JSONObject object) {

        try {
            JSONArray results = object.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject reviews = results.getJSONObject(i);
                String author = reviews.getString("author");
                String reviewTexts = reviews.getString("content");
                itemReviewsList.add(new ItemReview(author, reviewTexts));
            }


        } catch (JSONException e) {
            e.printStackTrace();

        } finally {
            return itemReviewsList;
        }

    }

    // from bitmap to String base64

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void onClicked() {
//        mypreference = getActivity().getSharedPreferences("moviepref", Context.MODE_PRIVATE);
        mypreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editorpref = mypreference.edit();
        Gson gson = new Gson();
        //retrieve the stored ArrayList of fovorite movies as json format
        String storedJsonList = mypreference.getString("storedjsonList", "");
        // get the type of this json format
        Type type = new TypeToken<ArrayList<ItemsClass>>() {
        }.getType();
        ArrayList<ItemsClass> movieArrayList = new ArrayList<ItemsClass>();
        //create array  list of the retrieved json
        movieArrayList = gson.fromJson(storedJsonList, type);
        // store it in my Arraylist
        movieArrayList = gson.fromJson(storedJsonList, type);

//        favo.setSelected(va[0]);
        ischecked = va[0];
        if (va[0] == true) {

            if (movieArrayList == null) {
                Toast.makeText(getActivity(), "you have favo this movie with id of " + id, Toast.LENGTH_SHORT).show();
                favo.setImageResource(R.drawable.lliked);

                serItems.add(new ItemsClass(StoredPoster, thetitle, thereleaseDate, theoverView, therating, id));
                serItems.size();
                //convert all the arraylist with the old and new Items to Json format
                String jsonList = gson.toJson(serItems);
                //save to sharedPreference
                editorpref.putString("storedjsonList", jsonList);
                editorpref.commit();
                va[0] = !va[0];

            } else if (movieArrayList != null) {
                serItems = movieArrayList;
                for (int i = 0; i < serItems.size(); i++) {
                    int detectId = serItems.get(i).getId();
                    if (serItems.size() > 0 && detectId == id) {
                        Toast.makeText(getActivity(), "you already have this movie in favorite list ", Toast.LENGTH_SHORT).show();
                        favo.setImageResource(R.drawable.lliked);
                        break;
                    } else if (serItems.size() > 0 && detectId != id && i < serItems.size() - 1) {
//                                favo.setImageResource(R.drawable.liked);
                        continue;

                    } else if (detectId != id && i == serItems.size() - 1 && i > serItems.size() - 2) {
                        favo.setImageResource(R.drawable.lliked);
                        Toast.makeText(getActivity(), "you have favo this movie with id of " + id, Toast.LENGTH_SHORT).show();
                        serItems.add(new ItemsClass(StoredPoster, thetitle, thereleaseDate, theoverView, therating, id));
                        serItems.size();
                        //convert all the arraylist with the old and new Items to Json format
                        String jsonList = gson.toJson(serItems);
                        //save to sharedPreference
                        editorpref.putString("storedjsonList", jsonList);
                        editorpref.commit();
                        va[0] = !va[0];
                        break;
                    }

                }
            }

        } else {
            if (movieArrayList != null) {
                serItems = movieArrayList;
                for (int i = 0; i < serItems.size(); i++) {
                    int detectId = serItems.get(i).getId();
                    if (i < serItems.size() && serItems.size() > 0 && detectId == id) {
                        Toast.makeText(getActivity(), "you have deleted this movie this movie with id of " + id, Toast.LENGTH_SHORT).show();
                        mypreference = getActivity().getSharedPreferences("moviepref", Context.MODE_PRIVATE);
                        editorpref.putString("storedPoster", "");
                        editorpref.putString("storedTitle", "");
                        editorpref.putString("storedOverview", "");
                        editorpref.putString("storedDate", "");
                        editorpref.putFloat("storedrating", (float) 0.0);
                        editorpref.commit();
                        va[0] = !va[0];
//
                        break;
                    } else if (detectId != id && i < serItems.size() - 1) {
                        continue;
                    } else if (detectId == id && i == serItems.size() - 1) {
                        Toast.makeText(getActivity(), " actually you don't have this movie in your favorite list ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

}


