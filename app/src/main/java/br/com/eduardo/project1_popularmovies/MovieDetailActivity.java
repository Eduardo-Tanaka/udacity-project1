


package br.com.eduardo.project1_popularmovies;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.eduardo.project1_popularmovies.adapter.MovieAdapter;
import br.com.eduardo.project1_popularmovies.adapter.TrailerAdapter;
import br.com.eduardo.project1_popularmovies.endpoints.MoviesService;
import br.com.eduardo.project1_popularmovies.models.Result;
import br.com.eduardo.project1_popularmovies.models.ResultTrailer;
import br.com.eduardo.project1_popularmovies.models.Trailer;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.id.list;
import static java.security.AccessController.getContext;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mTitle;
    private ImageView mImage;

    private TextView mDateContent;
    private TextView mVoteContent;

    private TextView mOverview;
    private ScrollView mScroll;
    private RelativeLayout mMovieDetail;

    private MoviesService service;
    private ResultTrailer mResultTrailer;

    private LinearLayout mLinearLayout;
    private Toast mToast;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setText(intent.getStringExtra("title"));

        mImage = (ImageView) findViewById(R.id.iv_movie_detail);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + intent.getStringExtra("image")).into(mImage);

        mDateContent = (TextView) findViewById(R.id.tv_date_content);
        mDateContent.setText(intent.getStringExtra("date"));

        mVoteContent = (TextView) findViewById(R.id.tv_vote_content);
        mVoteContent.setText(intent.getStringExtra("vote")+"/10");

        mOverview = (TextView) findViewById(R.id.tv_overview);
        mOverview.setText(intent.getStringExtra("overview"));

        id = intent.getStringExtra("id");

        if(savedInstanceState == null || !savedInstanceState.containsKey("videos")) {
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
            mResultTrailer = savedInstanceState.getParcelable("videos");
            final TrailerAdapter adapter = new TrailerAdapter(getApplicationContext(), R.layout.trailers_list_item, mResultTrailer.results);
            ListView mListTrailers = (ListView) findViewById(R.id.lv_trailers);
            mListTrailers.setAdapter(adapter);

            mListTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Trailer t = adapter.getItem(i);

                    String movieurl = "https://www.youtube.com/watch?v=" + t.key;
                    Intent tostart = new Intent(Intent.ACTION_VIEW, Uri.parse(movieurl));
                    startActivity(tostart);

                    adapter.notifyDataSetChanged();
                }
            });
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
                    final TrailerAdapter adapter = new TrailerAdapter(getApplicationContext(), R.layout.trailers_list_item, mResultTrailer.results);
                    ListView mListTrailers = (ListView) findViewById(R.id.lv_trailers);
                    mListTrailers.setAdapter(adapter);

                    mListTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Trailer t = adapter.getItem(i);
                            String movieurl = "https://www.youtube.com/watch?v=" + t.key;
                            Intent tostart = new Intent(Intent.ACTION_VIEW, Uri.parse(movieurl));
                            startActivity(tostart);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                }
            });
        }
    }

    public void Review(View v){
        Intent intent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void Favorite(View v){
        Intent intent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("videos", mResultTrailer);
        super.onSaveInstanceState(outState);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}