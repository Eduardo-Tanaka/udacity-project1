package br.com.eduardo.project1_popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.eduardo.project1_popularmovies.R;
import br.com.eduardo.project1_popularmovies.models.Movie;
import br.com.eduardo.project1_popularmovies.models.Result;
import br.com.eduardo.project1_popularmovies.models.ResultTrailer;
import br.com.eduardo.project1_popularmovies.models.Trailer;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eduardo on 01/01/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private int mNumberItems;
    private List<Trailer> mDataSet;
    private Context mContext;

    final private TrailerAdapter.ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public TrailerAdapter(int numberOfItems, List<Trailer> trailers, Context context, TrailerAdapter.ListItemClickListener listener) {
        mNumberItems = numberOfItems;
        mDataSet = trailers;
        mContext = context;
        mOnClickListener = listener;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailerAdapter.TrailerViewHolder viewHolder = new TrailerAdapter.TrailerViewHolder(view);

        Log.d(TAG, "onCreateViewHolder");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        @BindView(R.id.iv_play) ImageView imgMovie;
        @BindView(R.id.tv_trailer) TextView txtTrailer;

        public TrailerViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            txtTrailer.setText(itemView.getResources().getString(R.string.trailer) + " " + (listIndex + 1));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
