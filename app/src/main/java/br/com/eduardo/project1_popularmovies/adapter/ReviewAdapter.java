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
import br.com.eduardo.project1_popularmovies.models.Review;

/**
 * Created by Eduardo on 01/01/2017.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    public ReviewAdapter(Context context, int resourceId, List<Review> reviews) {
        super(context, resourceId, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review result = getItem(position);
        Log.d(TAG, position+"");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_list_item, parent, false);
        }

        TextView author = (TextView) convertView.findViewById(R.id.tv_review_author);
        author.setText("Review By: " + result.author);

        TextView review = (TextView) convertView.findViewById(R.id.tv_review);
        review.setText(result.content);

        return convertView;
    }
}
