package br.com.eduardo.project1_popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eduardo on 31/12/2016.
 */

public class ResultTrailer implements Parcelable {

    public String id;
    public List<Trailer> results;

    public ResultTrailer(String vId, List<Trailer> vResults)
    {
        this.id = vId;
        this.results = vResults;
    }


    private ResultTrailer(Parcel in){
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
    public static Creator<ResultTrailer> CREATOR = new Creator<ResultTrailer>() {

        @Override
        public ResultTrailer createFromParcel(Parcel source) {
            return new ResultTrailer(source);
        }

        @Override
        public ResultTrailer[] newArray(int size) {
            return new ResultTrailer[size];
        }

    };
}
