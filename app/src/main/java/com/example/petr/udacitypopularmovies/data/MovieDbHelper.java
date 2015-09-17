package com.example.petr.udacitypopularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by petr on 10.09.2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movie";
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_IS_FAVORITE + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RUNTIME + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL" + ");";

        Log.e("mytag:crate table:", SQL_CREATE_MOVIE_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

//    public static ArrayList<Movie> getMovies(String selected) {
//        ArrayList<Movie>  movies = new ArrayList<>();
//
//            // 1. build the query
//            String query = "SELECT  * FROM " + MovieContract.MovieEntry.TABLE_NAME + " ORDER BY " + selected  + " DESC";
//
//            // 2. get reference to writable DB
//            SQLiteDatabase db = new MovieDbHelper.getWritableDatabase();
//            Cursor cursor = db.rawQuery(query, null);
//
//            // 3. go over each row, build book and add it to list
//            Movie movie = null;
//            if (cursor.moveToFirst()) {
//                do {
//                    int db_id = Integer.parseInt(cursor.getString(0));
//                    String title = cursor.getString(1);
//                    String overview = cursor.getString(2);
//                    String release_date = cursor.getString(3);
//                    double popularity = cursor.getDouble(4);
//                    int vote_average = cursor.getInt(5);
//                    int vote_count = cursor.getInt(6);
//                    String poster_path = cursor.getString(7);
//                    movie = new Movie(db_id,title,overview,release_date,popularity,vote_average,vote_count,poster_path);
//                    movies.add(movie);
//                } while (cursor.moveToNext());
//            }
//
//            Log.d("getAllMovies", movies.toString());
//
//            return movies;
//        }
    }

