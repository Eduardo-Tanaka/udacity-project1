package br.com.eduardo.project1_popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eduardo on 31/12/2016.
 */

public class Result implements Parcelable {

    public String page;
    public List<Movie> results;

    public Result(String vPage, List<Movie> vResults)
    {
        this.page = vPage;
        this.results = vResults;
    }


    private Result(Parcel in){
        page = in.readString();
        results = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(page);
        dest.writeList(results);
    }

    // Method to recreate a Question from a Parcel
    public static Creator<Result> CREATOR = new Creator<Result>() {

        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }

    };
}
