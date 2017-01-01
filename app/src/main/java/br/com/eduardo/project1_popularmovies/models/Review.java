package br.com.eduardo.project1_popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eduardo on 01/01/2017.
 */

public class Review implements Parcelable{

    public String content;
    public String author;

    public Review(String vContent, String vAuthor)
    {
        this.content = vContent;
        this.author = vAuthor;
    }

    private Review(Parcel in){
        content = in.readString();
        author = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(author);
    }

    public final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };

}
