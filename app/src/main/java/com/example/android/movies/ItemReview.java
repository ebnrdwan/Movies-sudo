package com.example.android.movies;

/**
 * Created by Abdulrhman on 16/11/2016.
 */
public class ItemReview {
    private String author;
    private String review;

    public ItemReview(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
