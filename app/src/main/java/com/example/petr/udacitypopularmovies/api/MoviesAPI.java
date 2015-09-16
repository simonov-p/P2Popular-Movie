package com.example.petr.udacitypopularmovies.api;

import com.example.petr.udacitypopularmovies.objects.Movie;
import com.example.petr.udacitypopularmovies.objects.Movies;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by petr on 15.09.2015.
 */
public interface MoviesAPI {

    @GET("/3/discover/movie")
    void getMovies(@Query("sort_by")String sort,@Query("api_key")String key, @Query("vote_count.gte")String minVoteCount,Callback<Movies> response);

    @GET("/3/movie/{id}")
    void getMovieMoreInfo(@Path("id")int id,@Query("api_key")String key, Callback<Movie> response);

}
