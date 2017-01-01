package br.com.eduardo.project1_popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import br.com.eduardo.project1_popularmovies.adapter.MovieAdapter;
import br.com.eduardo.project1_popularmovies.endpoints.MoviesService;
import br.com.eduardo.project1_popularmovies.models.Movie;
import br.com.eduardo.project1_popularmovies.models.Result;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity  implements MovieAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MoviesService service;

    private MovieAdapter mAdapter;
    private RecyclerView mMoviesList;
    private String searchType = "popular";

    private Toast mToast;

    private Result mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMoviesList.setLayoutManager(layoutManager);

        mMoviesList.setHasFixedSize(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Log.i(TAG, "no savedInstanceState");

            if(isOnline()){
                populateRecyclerView(searchType);
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
            mResult = savedInstanceState.getParcelable("movies");
            mAdapter = new MovieAdapter(mResult.results.size()/2, mResult.results, MainActivity.this, MainActivity.this);
            mMoviesList.setAdapter(mAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movies", mResult);
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
        if(isOnline()){
            if(id == R.id.main_menu_popular){
                searchType = "popular";
                populateRecyclerView(searchType);
            } else if(id == R.id.main_menu_rated){
                searchType = "rate";
                populateRecyclerView(searchType);
            }
        } else {
            // No internet connectivity
            if(mToast != null){
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), "No internet connectivity...", Toast.LENGTH_LONG);
            mToast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateRecyclerView(String searchType){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.themoviedb.org/3/movie/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        service = restAdapter.create(MoviesService.class);

        if(searchType.equals("popular")){
            service.getMostPopularMovies(new Callback<Result>() {
                @Override
                public void success(Result result, Response response) {
                    Log.i(TAG, "Popular: " + String.valueOf(result.results.size()));
                    mResult = result;

                    mAdapter = new MovieAdapter(mResult.results.size()/2, mResult.results, MainActivity.this, MainActivity.this);
                    mMoviesList.setAdapter(mAdapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                }
            });
        } else if(searchType.equals("rate")){
            service.getHighestRatedMovies(new Callback<Result>() {
                @Override
                public void success(Result result, Response response) {
                    Log.i(TAG, "Rate: " +  String.valueOf(result.results.size()));
                    mResult = result;

                    mAdapter = new MovieAdapter(mResult.results.size()/2, mResult.results, MainActivity.this, MainActivity.this);
                    mMoviesList.setAdapter(mAdapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                }
            });
        }
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
        startActivity(intent);
    }
}
