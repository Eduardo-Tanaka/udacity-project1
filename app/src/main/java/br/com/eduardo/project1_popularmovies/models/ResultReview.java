package br.com.eduardo.project1_popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eduardo on 01/01/2017.
 */

public class ResultReview implements Parcelable{

    public String id;
    public List<Review> results;

    public ResultReview(String vId, List<Review> vResults)
    {
        this.id = vId;
        this.results = vResults;
    }

    private ResultReview(Parcel in){
        id = in.readString();
        results = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeList(results);
    }

    // Method to recreate a Question from a Parcel
    public static Parcelable.Creator<ResultReview> CREATOR = new Parcelable.Creator<ResultReview>() {

        @Override
        public ResultReview createFromParcel(Parcel source) {
            return new ResultReview(source);
        }

        @Override
        public ResultReview[] newArray(int size) {
            return new ResultReview[size];
        }

    };
}
