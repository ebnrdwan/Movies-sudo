package com.example.android.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdulrhman on 20/10/2016.
 */
public class adapterMovie extends ArrayAdapter {
    String title;
    String overview;
    String releaseDate;
    double rate;
    String storedPoster;
    String loadedPoster;
    int id;
    boolean ischecked = true;
    ArrayList<ItemsClass> serItems;
    SharedPreferences mypreference;
    final boolean[] va = {true};
    Bitmap theposterImage;
    ImageView favoImageButton;
    View progressBar;
    Bitmap holderBitmap;
    handlerCallback adapterhandlerCallback;


    public adapterMovie(Context context, List<ItemsClass> itemsArraylist, handlerCallback inerface) {
        super(context, 0, itemsArraylist);
        adapterhandlerCallback = inerface;
    }

    public void setCurrentItem(handlerCallback callback) {
        adapterhandlerCallback = callback;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rootview = convertView;
        if (rootview == null) {
            rootview = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }
        final ItemsClass currentItem;
        currentItem = (ItemsClass) getItem(position);
        final ImageView poster = (ImageView) rootview.findViewById(R.id.posterItem);
        progressBar = rootview.findViewById(R.id.ImageloadingBar);

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("DEDE", currentItem.toString());
//                Intent gridIntent = new Intent(getContext(), DetailActivity.class)
//                        .putExtra("ItemObject", currentItem);
//                getContext().startActivity(gridIntent);
                ItemsClass dd = currentItem;
                if (adapterhandlerCallback != null){
                    if (currentItem!=null)
                    adapterhandlerCallback.onItemSelected(currentItem);
                }

            }
        });
        favoImageButton = (ImageView) rootview.findViewById(R.id.favoButton);
        serItems = new ArrayList<ItemsClass>();

        if (isConnected()) {
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String sortBy = preferences.getString("sort_by", "-1");

                if (sortBy.equals("myfavorite")) {
                    String storedPoster = currentItem.getPoster();
                    if (storedPoster != null) {
                        Bitmap image = decodeBase64(storedPoster);
                        poster.setImageBitmap(image);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                Picasso.with(getContext()).load(currentItem.getPoster()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        theposterImage = bitmap;
                        progressBar.setVisibility(View.GONE);
                        poster.setImageBitmap(bitmap);


                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
                loadedPoster = currentItem.getPoster();
                title = currentItem.getTitle();
                overview = currentItem.getOverview();
                releaseDate = currentItem.getReleaseDate();
                rate = currentItem.getrating();
                id = currentItem.getId();


            } catch (Exception e) {
//                Toast.makeText(getContext(), "can't load the image of the trailer", Toast.LENGTH_LONG).show();
            }

            favoImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onClicked();
//                    favoImageButton.clearFocus();
                }
            });
        } else {
            SharedPreferences mypreference = getContext().getSharedPreferences("moviepref", Context.MODE_PRIVATE);
            String storedPoster = mypreference.getString("storedPoster", "");
            String posterImage = currentItem.getPoster();
            if (posterImage != null) {
                Bitmap image = decodeBase64(posterImage);
                poster.setImageBitmap(image);
                progressBar.setVisibility(View.GONE);
            }

        }

        Log.d("abdo", title + " \n" + overview + " \n" + releaseDate + " \n" + rate);

        return rootview;
    }

    public static void setPoster() {
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

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    public void onClicked() {
//        mypreference = getContext().getSharedPreferences("moviepref", Context.MODE_PRIVATE);
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
        // store it in my Arraylist
        movieArrayList = gson.fromJson(storedJsonList, type);

//        favo.setSelected(va[0]);
        ischecked = va[0];
        if (va[0] == true) {

            if (movieArrayList == null) {

                favoImageButton.setImageResource(R.drawable.liked);

                String loadedPosterString = encodeTobase64(theposterImage);


                serItems.add(new ItemsClass(loadedPosterString, title, releaseDate, overview, rate, id));

                Toast.makeText(getContext(), "you have favo this movie with id of " + id + "and you list size is" + serItems.size(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "you already have this movie in favorite list ", Toast.LENGTH_SHORT).show();
                        favoImageButton.setImageResource(R.drawable.liked);
                        break;
                    } else if (serItems.size() > 0 && detectId != id && i < serItems.size() - 1) {

                        continue;

                    } else if (detectId != id && i == serItems.size() - 1 && i > serItems.size() - 2) {
                        Toast.makeText(getContext(), "you have favo this movie with id of " + id, Toast.LENGTH_SHORT).show();
                        favoImageButton.setImageResource(R.drawable.like);
                        serItems.add(new ItemsClass(storedPoster, title, releaseDate, overview, rate, id));
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
                        Toast.makeText(getContext(), "you have deleted this movie this movie with id of " + id, Toast.LENGTH_SHORT).show();
                        favoImageButton.setImageResource(R.drawable.like);
                        mypreference = getContext().getSharedPreferences("moviepref", Context.MODE_PRIVATE);
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
                        Toast.makeText(getContext(), " actually you don't have this movie in your favorite list ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }


}
