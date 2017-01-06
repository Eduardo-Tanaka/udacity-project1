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

import java.util.List;

import br.com.eduardo.project1_popularmovies.R;
import br.com.eduardo.project1_popularmovies.models.Review;
import br.com.eduardo.project1_popularmovies.models.Trailer;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eduardo on 01/01/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private int mNumberItems;
    private List<Review> mDataSet;
    private Context mContext;

    public ReviewAdapter(int numberOfItems, List<Review> reviews, Context context) {
        mNumberItems = numberOfItems;
        mDataSet = reviews;
        mContext = context;
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewAdapter.ReviewViewHolder viewHolder = new ReviewAdapter.ReviewViewHolder(view);

        Log.d(TAG, "onCreateViewHolder");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_review_author) TextView txtAuthor;
        @BindView(R.id.tv_review) TextView txtReview;

        public ReviewViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(int listIndex) {
            txtAuthor.setText(itemView.getResources().getString(R.string.review_by) + " " + mDataSet.get(listIndex).author);
            txtReview.setText(mDataSet.get(listIndex).content);
        }
    }
}
