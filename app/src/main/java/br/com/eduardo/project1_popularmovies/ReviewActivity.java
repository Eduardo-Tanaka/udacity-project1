package br.com.eduardo.project1_popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import br.com.eduardo.project1_popularmovies.adapter.ReviewAdapter;
import br.com.eduardo.project1_popularmovies.adapter.TrailerAdapter;
import br.com.eduardo.project1_popularmovies.endpoints.MoviesService;
import br.com.eduardo.project1_popularmovies.models.ResultReview;
import br.com.eduardo.project1_popularmovies.models.ResultTrailer;
import br.com.eduardo.project1_popularmovies.models.Trailer;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReviewActivity extends AppCompatActivity {

    private final String TAG = ReviewActivity.class.getSimpleName();

    private MoviesService service;
    private String id;
    private ResultReview mResultReview;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        if(savedInstanceState == null || !savedInstanceState.containsKey("reviews")) {
            Log.i(TAG, "no savedInstanceState");

            if(isOnline()){
                populateListView(id);
            } else {
                // No internet connectivity
                if(mToast != null){
                    mToast.cancel();
                }
                mToast = Toast.makeText(getApplicationContext(), "No internet connectivity...", Toast.LENGTH_LONG);
                mToast.show();
            }
        }
        else {
            Log.i(TAG, "savedInstanceState");
            mResultReview = savedInstanceState.getParcelable("reviews");
            final ReviewAdapter adapter = new ReviewAdapter(getApplicationContext(), R.layout.reviews_list_item, mResultReview.results);
            ListView mListTrailers = (ListView) findViewById(R.id.lv_reviews);
            mListTrailers.setAdapter(adapter);
        }
    }

    private void populateListView(String id) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.themoviedb.org/3/movie/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        service = restAdapter.create(MoviesService.class);

        if(isOnline()){
            service.getReviews(id, new Callback<ResultReview>() {
                @Override
                public void success(ResultReview resultReview, Response response) {
                    Log.d(TAG, resultReview.results.size()+"");
                    mResultReview = resultReview;
                    final ReviewAdapter adapter = new ReviewAdapter(getApplicationContext(), R.layout.reviews_list_item, mResultReview.results);
                    ListView mListTrailers = (ListView) findViewById(R.id.lv_reviews);
                    mListTrailers.setAdapter(adapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("reviews", mResultReview);
        super.onSaveInstanceState(outState);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}