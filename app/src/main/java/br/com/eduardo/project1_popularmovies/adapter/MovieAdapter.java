package br.com.eduardo.project1_popularmovies.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.eduardo.project1_popularmovies.R;
import br.com.eduardo.project1_popularmovies.models.Movie;

/**
 * Created by Eduardo on 31/12/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private int mNumberItems;
    private List<Movie> mDataSet;
    private Context mContext;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdapter(int numberOfItems, List<Movie> movies, Context context, ListItemClickListener listener) {
        mNumberItems = numberOfItems;
        mDataSet = movies;
        mContext = context;
        mOnClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        Log.d(TAG, "onCreateViewHolder");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ImageView imgMovie1;
        ImageView imgMovie2;

        public MovieViewHolder(final View itemView) {
            super(itemView);

            imgMovie1 = (ImageView) itemView.findViewById(R.id.iv_movie1);
            imgMovie2 = (ImageView) itemView.findViewById(R.id.iv_movie2);

            imgMovie1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnClickListener.onListItemClick(clickedPosition * 2);
                }
            });
            imgMovie2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnClickListener.onListItemClick((clickedPosition * 2) + 1);
                }
            });
        }

        void bind(int listIndex) {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + mDataSet.get(listIndex * 2).poster_path).into(imgMovie1);
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + mDataSet.get((listIndex * 2) + 1).poster_path).into(imgMovie2);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
