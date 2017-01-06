package br.com.eduardo.project1_popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Eduardo on 31/12/2016.
 */

public class Movie implements Parcelable {

    public String poster_path;
    public String title;
    public String release_date;
    public String vote_average;
    public String overview;
    public String id;

    public Movie(String vPosterPath, String vTitle, String vVoteAverage, String vOverview, String vReleaseDate, String vId)
    {
        this.poster_path = vPosterPath;
        this.title = vTitle;
        this.vote_average = vVoteAverage;
        this.overview = vOverview;
        this.release_date = vReleaseDate;
        this.id = vId;
    }

    private Movie(Parcel in){
        poster_path = in.readString();
        title = in.readString();
        vote_average = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(title);
        dest.writeString(vote_average);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(id);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
