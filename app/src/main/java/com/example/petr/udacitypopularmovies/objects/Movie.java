package com.example.petr.udacitypopularmovies.objects;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by petr on 09.09.2015.
 */
public class Movie {
    public String title;
    public String overview;
    int vote_average;
    int vote_count;
    Uri poster_path;
    Uri backdrop_path;
    String release_date;
    double popularity;
    int db_id;
    private String POSTER_BASE_URI = "http://image.tmdb.org/t/p/";
    private String size = "w500";

    public Movie (JSONObject object) throws JSONException {
        this.title = object.getString("title");
        this.overview = object.getString("overview");
        this.vote_average = object.getInt("vote_average");
        this.vote_count = object.getInt("vote_count");
        // remove first / symbol
        String posterPath = object.getString("poster_path").substring(1);
        this.poster_path = getPosterUri(posterPath,size);
        this.backdrop_path = getPosterUri(object.getString("backdrop_path").substring(1),size);
        this.release_date = object.getString("release_date");
        this.popularity = object.getDouble("popularity");
        this.db_id = object.getInt("id");
    }

    private Uri getPosterUri(String secondaryUri, String size){
        Uri uri = Uri.parse(POSTER_BASE_URI).buildUpon()
                .appendPath(size)
                .appendPath(secondaryUri)
                .build();
        return uri;
    }
}
