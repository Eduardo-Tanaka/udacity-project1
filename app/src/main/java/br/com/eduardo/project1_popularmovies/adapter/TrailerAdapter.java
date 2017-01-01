package br.com.eduardo.project1_popularmovies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.eduardo.project1_popularmovies.R;
import br.com.eduardo.project1_popularmovies.models.Result;
import br.com.eduardo.project1_popularmovies.models.ResultTrailer;
import br.com.eduardo.project1_popularmovies.models.Trailer;

/**
 * Created by Eduardo on 01/01/2017.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer>{

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    public TrailerAdapter(Context context, int resourceId, List<Trailer> trailers) {
        super(context, resourceId, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer result = getItem(position);
        Log.d(TAG, position+"");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailers_list_item, parent, false);
        }

        TextView trailer = (TextView) convertView.findViewById(R.id.tv_trailer);
        trailer.setText("Trailer " + (position + 1));

        return convertView;
    }
}
