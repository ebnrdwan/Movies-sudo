package com.example.android.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Abdulrhman on 16/11/2016.
 */
public class adapterTrailer extends ArrayAdapter {

    int id = Detail_fragment.id;

    ImageView trailer;
    public String imageTrailerkey;


    public adapterTrailer(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootview = convertView;
        if (rootview == null) {
            rootview = LayoutInflater.from(getContext()).inflate(R.layout.trailer_layout, parent, false);
        }
        AndroidNetworking.initialize(getContext());
        ItemTrailer  currentItem = (ItemTrailer) getItem(position);

        String titleTrailer = currentItem.getTitle();
        imageTrailerkey = currentItem.getKey();
        TextView title = (TextView) rootview.findViewById(R.id.trailerTitle);
        title.setText(titleTrailer);
        trailer = (ImageView) rootview.findViewById(R.id.tubeimage);
        String imageUrl = "http://img.youtube.com/vi/" + imageTrailerkey + "/default.jpg";
        Picasso.with(getContext()).load(imageUrl).into(trailer);



//        Toast.makeText(getContext(), "successfully loaded the image of the trailer "+titleTrailer+" "+imageTrailerkey, Toast.LENGTH_SHORT).show();


        return rootview;
    }



}
