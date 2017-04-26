package com.example.android.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abdulrhman on 20/10/2016.
 */
public class ItemsClass implements Parcelable {

    private String poster ;
    private String title ;
    private String releaseDate ;
    private double rating ;
    private String overview ;
    private int id ;

    public ItemsClass(String poster , String title, String releaseDate, String overview, double rating, int id) {
        this.poster = poster;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.overview = overview;
        this.id= id;
    }

    public ItemsClass(String poster) {
        this.poster = poster;
    }

    protected ItemsClass(Parcel in) {
        this.poster = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
        this.rating = in.readDouble();
        this.id = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static final Creator<ItemsClass> CREATOR = new Creator<ItemsClass>() {
        @Override
        public ItemsClass createFromParcel(Parcel in) {
            return new ItemsClass(in);
        }

        @Override
        public ItemsClass[] newArray(int size) {
            return new ItemsClass[size];
        }
    };

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getrating() {
        return rating;
    }

    public void setrating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeDouble(rating);
        parcel.writeInt(id);

    }
}
