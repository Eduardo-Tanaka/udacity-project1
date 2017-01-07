package br.com.eduardo.project1_popularmovies.endpoints;

import br.com.eduardo.project1_popularmovies.models.Result;
import br.com.eduardo.project1_popularmovies.models.ResultReview;
import br.com.eduardo.project1_popularmovies.models.ResultTrailer;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Eduardo on 31/12/2016.
 */

public interface MoviesService {

    // Replace your api key here!
    String KEY = "a5a7ec63fb3fc78cf7a16e8e2b98991d";

    @GET("/popular?api_key=" + KEY)
    void getMostPopularMovies(Callback<Result> callback);

    @GET("/top_rated?api_key=" + KEY)
    void getHighestRatedMovies(Callback<Result> callback);

    @GET("/{id}/videos?api_key=" + KEY)
    void getVideos(@Path("id") String id, Callback<ResultTrailer> callback);

    @GET("/{id}/reviews?api_key=" + KEY)
    void getReviews(@Path("id") String id, Callback<ResultReview> callback);
}
