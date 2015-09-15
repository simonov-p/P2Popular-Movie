package com.example.petr.udacitypopularmovies.api;

import com.example.petr.udacitypopularmovies.objects.Movies;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by petr on 15.09.2015.
 */
public interface MoviesAPI {
    @GET("/3/discover/movie?sort_by=popularity.desc&api_key=faabcad1fcaddf30f757c38d94d44bc0&vote_count.gte=1000")
    void getMovies(Callback<Movies> response);
}
