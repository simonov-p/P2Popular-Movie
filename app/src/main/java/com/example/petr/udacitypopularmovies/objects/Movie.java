package com.example.petr.udacitypopularmovies.objects;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.petr.udacitypopularmovies.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

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
    public int runtime;
    public boolean isFavorite = false;

    public List<Review> reviews = new ArrayList<>();
    public List<Trailer> trailers = new ArrayList<>();

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
                "isfav='" + isFavorite + '\'' +
                ", vote_average=" + vote_average +
                ", vote_count=" + vote_count +
                ", popularity=" + popularity +
                ", poster_path=" + poster_path +
                ", release_date='" + release_date + '\'' +
                ", db_id=" + id +
                ", runtime=" + runtime +
                ", trailers=" + trailers +
                ", reviews=" + reviews +
                ", overview='" + overview + '\'' +
                '}';
    }

    public ContentValues putMovieToCV (){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, vote_average);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, vote_count);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, poster_path);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, release_date);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, runtime);
        return contentValues;
    }

    public class Reviews {
        private int id;
        private String page;
        private String total_pages;
        private String total_results;
        public List<Review> results;

        @Override
        public String toString() {
            return "Reviews{" +
                    "id=" + id +
                    ", is fav='" + String.valueOf(isFavorite) + '\'' +
                    ", page='" + page + '\'' +
                    ", total_pages='" + total_pages + '\'' +
                    ", total_results='" + total_results + '\'' +
                    ", results=" + results +
                    '}';
        }
    }

    public class Review{
        private String id;
        private String author;
        private String content;
        private String url;

        @Override
        public String toString() {
            return "Review{" +
                    "id=" + id +
                    ", author='" + author + '\'' +
                    ", content='" + content + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class Trailers {
        private int id;
        public List<Trailer> results;

        @Override
        public String toString() {
            return "Trailers{" +
                    "id=" + id +
                    ", results=" + results +
                    '}';
        }
    }

    public class Trailer{
        private String id;
        private String iso_639_1;
        private String key;
        private String name;
        private String site;
        private String size;
        private String type;

        @Override
        public String toString() {
            return "Trailer{" +
                    "id='" + id + '\'' +
                    ", iso_639_1='" + iso_639_1 + '\'' +
                    ", key='" + key + '\'' +
                    ", name='" + name + '\'' +
                    ", site='" + site + '\'' +
                    ", size='" + size + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
