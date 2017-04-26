package com.example.android.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abdulrhman on 16/11/2016.
 */
public class adapterReview extends ArrayAdapter {
    String author;
    String reviewTexts;
    List<ItemReview> reviewList;
    ItemReview currentItem;
    TextView authorText;
    TextView reviewText;
    public adapterReview(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootview = convertView;
        if (rootview == null) {
            rootview = LayoutInflater.from(getContext()).inflate(R.layout.review_layout, parent, false);
        }
        currentItem = (ItemReview) getItem(position);
        String author = currentItem.getAuthor();
        String review = currentItem.getReview();
        authorText = (TextView) rootview.findViewById(R.id.myauthor);
        authorText.setText(author);
        reviewText = (TextView) rootview.findViewById(R.id.myreview);
        reviewText.setText(review);
        return rootview;
    }







}
