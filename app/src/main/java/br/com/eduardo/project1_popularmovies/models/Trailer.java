package br.com.eduardo.project1_popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eduardo on 31/12/2016.
 */

public class Trailer implements Parcelable {

    public String key;

    public Trailer(String vKey)
    {
        this.key = vKey;
    }


    private Trailer(Parcel in){
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
    }

    public final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
