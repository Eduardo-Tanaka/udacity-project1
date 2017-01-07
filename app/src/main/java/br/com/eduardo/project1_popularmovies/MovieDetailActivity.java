package br.com.eduardo.project1_popularmovies;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.eduardo.project1_popularmovies.adapter.TrailerAdapter;
import br.com.eduardo.project1_popularmovies.data.MovieColumns;
import br.com.eduardo.project1_popularmovies.data.MovieContentProvider;
import br.com.eduardo.project1_popularmovies.endpoints.MoviesService;
import br.com.eduardo.project1_popularmovies.models.Movie;
import br.com.eduardo.project1_popularmovies.models.ResultTrailer;
import br.com.eduardo.project1_popularmovies.models.Trailer;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener{

    private final String TAG = MovieDetailActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.iv_movie_detail)
    ImageView mImage;
    @BindView(R.id.tv_date_content)
    TextView mDateContent;
    @BindView(R.id.tv_vote_content)
    TextView mVoteContent;
    @BindView(R.id.tv_overview)
    TextView mOverview;

    private MoviesService service;
    private TrailerAdapter mAdapter;
    @BindView(R.id.lv_trailers)
    RecyclerView mVideoList;

    private ResultTrailer mResultTrailer;

    private Toast mToast;
    private String id;

    private Movie mMovie;

    @BindView(R.id.button)
    Button mFav;
    @BindView(R.id.btnUnfavorite)
    Button mUnFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        mMovie = new Movie(intent.getStringExtra("image"), intent.getStringExtra("title"), intent.getStringExtra("vote"),
                intent.getStringExtra("overview"), intent.getStringExtra("date"), intent.getStringExtra("id"));

        Cursor cursor = MovieDetailActivity.this.getContentResolver().query(MovieContentProvider.Movies.withId(mMovie.id), null, null, null, null);
        if(cursor.getCount() == 1){
            mUnFav.setVisibility(View.VISIBLE);
            mFav.setVisibility(View.INVISIBLE);
        }

        mTitle.setText(mMovie.title);

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + mMovie.poster_path).into(mImage);

        mDateContent.setText(mMovie.release_date);

        mVoteContent.setText(mMovie.vote_average + "/10");

        mOverview.setText(mMovie.overview);

        id = mMovie.id;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mVideoList.setLayoutManager(layoutManager);

        mVideoList.setHasFixedSize(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey("videos")) {
            Log.d(TAG, "no savedInstanceState");

            populateListView(id);
        }
        else {
            Log.d(TAG, "savedInstanceState");

            mResultTrailer = savedInstanceState.getParcelable("videos");
            mAdapter = new TrailerAdapter(mResultTrailer.results.size(), mResultTrailer.results, MovieDetailActivity.this, MovieDetailActivity.this);
            mVideoList.setAdapter(mAdapter);
        }
    }

    private void populateListView(String id){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.themoviedb.org/3/movie/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        service = restAdapter.create(MoviesService.class);

        if(isOnline()){
            service.getVideos(id, new Callback<ResultTrailer>() {
                @Override
                public void success(ResultTrailer resultTrailer, Response response) {
                    Log.d(TAG, resultTrailer.results.size()+"");
                    mResultTrailer = resultTrailer;
                    mAdapter = new TrailerAdapter(mResultTrailer.results.size(), mResultTrailer.results, MovieDetailActivity.this, MovieDetailActivity.this);
                    mVideoList.setAdapter(mAdapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.retrofit_error), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            error(getString(R.string.no_internet_trailers));
        }
    }

    public void Review(View v){
        Intent intent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void Favorite(View v){
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                mFav.setVisibility(View.INVISIBLE);
                mUnFav.setVisibility(View.VISIBLE);
            }
        };

        ContentValues cv = new ContentValues();
        cv.put(MovieColumns.DATE, mMovie.release_date);
        cv.put(MovieColumns.TITLE, mMovie.title);
        cv.put(MovieColumns.POSTER, mMovie.poster_path);
        cv.put(MovieColumns.RATING, mMovie.vote_average);
        cv.put(MovieColumns.SYNOPSIS, mMovie.overview);
        cv.put(MovieColumns._ID, mMovie.id);
        Log.d(TAG, "MOVIE ID: " + mMovie.id);

        queryHandler.startInsert(200, null, MovieContentProvider.Movies.withId(mMovie.id), cv);
    }

    public void Unfavorite(View v){
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);
                mFav.setVisibility(View.VISIBLE);
                mUnFav.setVisibility(View.INVISIBLE);
            }
        };

        Log.d(TAG, "MOVIE ID: " + mMovie.id);
        queryHandler.startDelete(300, null, MovieContentProvider.Movies.withId(mMovie.id), null, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mResultTrailer != null){
            outState.putParcelable("videos", mResultTrailer);
        }
        super.onSaveInstanceState(outState);
    }

    private void error(String message) {
        /*if(mToast != null){
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG);
            mToast.show();*/
        Snackbar snackbar = Snackbar
            .make(findViewById(R.id.sv_movie), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    populateListView(id);
                }
            });

        snackbar.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Trailer t = mResultTrailer.results.get(clickedItemIndex);
        String movieurl = "https://www.youtube.com/watch?v=" + t.key;
        Intent tostart = new Intent(Intent.ACTION_VIEW, Uri.parse(movieurl));
        // Verify that the intent will resolve to an activity
        if (tostart.resolveActivity(getPackageManager()) != null) {
            startActivity(tostart);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.youtube), Toast.LENGTH_LONG).show();
        }
    }
}
