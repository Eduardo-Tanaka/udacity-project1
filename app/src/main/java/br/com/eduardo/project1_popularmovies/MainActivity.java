package br.com.eduardo.project1_popularmovies;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import br.com.eduardo.project1_popularmovies.adapter.MovieAdapter;
import br.com.eduardo.project1_popularmovies.data.MovieColumns;
import br.com.eduardo.project1_popularmovies.data.MovieContentProvider;
import br.com.eduardo.project1_popularmovies.endpoints.MoviesService;
import br.com.eduardo.project1_popularmovies.models.Movie;
import br.com.eduardo.project1_popularmovies.models.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity  implements MovieAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MoviesService service;

    private MovieAdapter mAdapter;
    @BindView(R.id.rv_movies) RecyclerView mMoviesList;
    private String searchType = "popular";

    private Toast mToast;

    private Result mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        SharedPreferences shared = getSharedPreferences("udacity_project1", MODE_PRIVATE);
        searchType = shared.getString("search_type", "popular");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(gridLayoutManager);

        mMoviesList.setHasFixedSize(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Log.i(TAG, "no savedInstanceState");

            populateRecyclerView(searchType);
        }
        else {
            Log.i(TAG, "savedInstanceState");
            mResult = savedInstanceState.getParcelable("movies");
            mAdapter = new MovieAdapter(mResult.results.size(), mResult.results, MainActivity.this, MainActivity.this);
            mMoviesList.setAdapter(mAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mResult != null){
            outState.putParcelable("movies", mResult);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.main_menu_popular){
            searchType = "popular";
            populateRecyclerView(searchType);
        } else if(id == R.id.main_menu_rated){
            searchType = "rate";
            populateRecyclerView(searchType);
        } else if(id == R.id.main_menu_favorite) {
            searchType = "favorite";
            populateRecyclerView(searchType);
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateRecyclerView(String searchType){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.themoviedb.org/3/movie/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        service = restAdapter.create(MoviesService.class);

        if(searchType.equals("favorite")){
            insertSharedPreference("favorite");
            Cursor cursor = MainActivity.this.getContentResolver().query(MovieContentProvider.Movies.CONTENT_URI, null, null, null, null);

            List<Movie> movies = new ArrayList<Movie>();
            while(cursor.moveToNext()){
                Log.d(TAG, cursor.getString(cursor.getColumnIndex(MovieColumns._ID)));
                Movie m = new Movie(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.RATING)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.SYNOPSIS)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns.DATE)),
                        cursor.getString(cursor.getColumnIndex(MovieColumns._ID)));
                movies.add(m);
            }
            mResult = new Result("1", movies);
            mAdapter = new MovieAdapter(mResult.results.size(), mResult.results, MainActivity.this, MainActivity.this);
            mMoviesList.setAdapter(mAdapter);
        } else {
            if(isOnline()){
                if(searchType.equals("popular")){
                    insertSharedPreference("popular");
                    service.getMostPopularMovies(new Callback<Result>() {
                        @Override
                        public void success(Result result, Response response) {
                            Log.i(TAG, "Popular: " + String.valueOf(result.results.size()));
                            mResult = result;

                            mAdapter = new MovieAdapter(mResult.results.size(), mResult.results, MainActivity.this, MainActivity.this);
                            mMoviesList.setAdapter(mAdapter);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, error.getMessage());
                            Toast.makeText(getApplicationContext(), getString(R.string.retrofit_error), Toast.LENGTH_LONG).show();
                        }
                    });
                } else if(searchType.equals("rate")){
                    insertSharedPreference("rate");
                    service.getHighestRatedMovies(new Callback<Result>() {
                        @Override
                        public void success(Result result, Response response) {
                            Log.i(TAG, "Rate: " +  String.valueOf(result.results.size()));
                            mResult = result;

                            mAdapter = new MovieAdapter(mResult.results.size(), mResult.results, MainActivity.this, MainActivity.this);
                            mMoviesList.setAdapter(mAdapter);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, error.getMessage());
                            Toast.makeText(getApplicationContext(), getString(R.string.retrofit_error), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                error(getString(R.string.no_internet));
            }
        }
    }

    private void error(String message) {
        /*if(mToast != null){
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG);
            mToast.show();*/
        Snackbar snackbar = Snackbar
            .make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    populateRecyclerView(searchType);
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
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra("title", mResult.results.get(clickedItemIndex).title);
        intent.putExtra("date", mResult.results.get(clickedItemIndex).release_date);
        intent.putExtra("image", mResult.results.get(clickedItemIndex).poster_path);
        intent.putExtra("vote", mResult.results.get(clickedItemIndex).vote_average);
        intent.putExtra("overview", mResult.results.get(clickedItemIndex).overview);
        intent.putExtra("id", mResult.results.get(clickedItemIndex).id);
        Log.d(TAG, "ID:" + mResult.results.get(clickedItemIndex).id);
        startActivity(intent);
    }

    private void insertSharedPreference(String type){
        SharedPreferences shared = getApplicationContext().getSharedPreferences("udacity_project1", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString("search_type", type);

        editor.commit();
    }
}
