package com.example.petr.udacitypopularmovies.objects;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.petr.udacitypopularmovies.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by petr on 09.09.2015.
 */
public class Movie implements Parcelable {
    public String title;
    public String overview;
    public int vote_average;
    public int vote_count;
    public Uri poster_path;
    Uri backdrop_path;
    public String release_date;
    public double popularity;
    public int db_id;
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

    public Movie(int db_id, String title, String overview, String release_date, double popularity,
                 int vote_average, int vote_count, String poster_path) {
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.poster_path = Uri.parse(poster_path);
        this.release_date = release_date;
        this.popularity = popularity;
        this.db_id = db_id;
    }

    private Uri getPosterUri(String secondaryUri, String size){
        Uri uri = Uri.parse(POSTER_BASE_URI).buildUpon()
                .appendPath(size)
                .appendPath(secondaryUri)
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
                ", overview='" + overview + '\'' +
                ", poster_path=" + poster_path +
                ", backdrop_path=" + backdrop_path +
                ", release_date='" + release_date + '\'' +
                ", db_id=" + db_id +
                ", POSTER_BASE_URI='" + POSTER_BASE_URI + '\'' +
                ", size='" + size + '\'' +
                ", mData=" + mData +
                '}';
    }

    public ContentValues putMovieToCV (){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, db_id);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, vote_average);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, vote_count);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, poster_path.toString());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
        return contentValues;
    }
}
