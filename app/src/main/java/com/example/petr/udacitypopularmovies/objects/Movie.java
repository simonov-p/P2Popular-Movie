package com.example.petr.udacitypopularmovies.objects;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.petr.udacitypopularmovies.data.MovieContract;

import java.util.Map;

/**
 * Created by petr on 09.09.2015.
 */
public class Movie implements Parcelable {
    private boolean adult;
    public String title;
    public String overview;
    public double vote_average;
    public int vote_count;
    public String poster_path;
    String backdrop_path;
    public String release_date;
    public double popularity;
    public int id;
    public int duration;

    public Map<String,String> trailers;
    public Map<String,String> reviews;



//    public Movie (JSONObject object) throws JSONException {
//        this.title = object.getString("title");
//        this.overview = object.getString("overview");
//        this.vote_average = object.getInt("vote_average");
//        this.vote_count = object.getInt("vote_count");
//        // remove first / symbol
//        String posterPath = object.getString("poster_path").substring(1);
//        this.poster_path = getPosterUri(posterPath);
//        this.backdrop_path = getPosterUri(object.getString("backdrop_path").substring(1)).toString();
//        this.release_date = object.getString("release_date");
//        this.popularity = object.getDouble("popularity");
//        this.id = object.getInt("id");
//    }
//
//    public Movie(int db_id, String title, String overview, String release_date, double popularity,
//                 int vote_average, int vote_count, String poster_path) {
//        this.title = title;
//        this.overview = overview;
//        this.vote_average = vote_average;
//        this.vote_count = vote_count;
//        this.poster_path = poster_path;
//        this.release_date = release_date;
//        this.popularity = popularity;
//        this.id = db_id;
//    }

    public Uri getPosterUri(){
        String POSTER_BASE_URI = "http://image.tmdb.org/t/p/";
        String size = "w500";
        Uri uri = Uri.parse(POSTER_BASE_URI).buildUpon()
                .appendPath(size)
                .appendPath(this.poster_path.substring(1))
                .build();
        return uri;
    }

    private int mData;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        mData = in.readInt();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", vote_average=" + vote_average +
                ", vote_count=" + vote_count +
                ", popularity=" + popularity +
                ", poster_path=" + poster_path +
                ", release_date='" + release_date + '\'' +
                ", db_id=" + id +
                ", duration=" + duration +
                ", trailers=" + trailers +
                ", reviews=" + reviews +
                ", overview='" + overview + '\'' +
                '}';
    }

    public ContentValues putMovieToCV (){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, id);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, vote_average);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, vote_count);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, poster_path.toString());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
        return contentValues;
    }
}
