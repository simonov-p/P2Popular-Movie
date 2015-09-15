package com.example.petr.udacitypopularmovies.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.petr.udacitypopularmovies.fragments.GridFragment;
import com.example.petr.udacitypopularmovies.objects.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by petr on 13.09.2015.
 */
public class FetchMovieTask extends AsyncTask<Void, String, String> {

    private GridFragment.FragmentCallback mFragmentCallback;

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private Context mContext;
    private String MOVIE_API_BASE_URL = "http://api.themoviedb.org/3";
    String baseURL;
    private Uri mBuiltUri;
    final String SORT_BY_PARAM = "sort_by";
    final String API_KEY_PARAM = "api_key";
    final String VOTE_COUNT_PARAM = "vote_count.gte";
    String api_key = "faabcad1fcaddf30f757c38d94d44bc0";
//    String api_key = mContext.getString(R.string.the_movieDB_API_key);
    public static final String REQUEST_VIDEOS = "/videos";
    public static final String REQUEST_REVIEWS = "/reviews";
    public static final String REQUEST_MORE_INFO = "";

    private ArrayList<Movie> mMovies = new ArrayList<>();


    public FetchMovieTask(Context context, String args, GridFragment.FragmentCallback fragmentCallback) {

        mContext = context;
        mFragmentCallback = fragmentCallback;
        baseURL = MOVIE_API_BASE_URL + "/discover/movie?";

        String sort = args + ".desc";
        String voteCountMinParam = "1000";

        mBuiltUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(SORT_BY_PARAM, sort)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(VOTE_COUNT_PARAM, voteCountMinParam)
                .build();
    }
    public FetchMovieTask(Context context, Movie movie, String movieItem, GridFragment.FragmentCallback fragmentCallback) {

        mContext = context;
        baseURL = MOVIE_API_BASE_URL + "/movie/" + movie.id + movieItem  + "?";
        mFragmentCallback = fragmentCallback;

        mBuiltUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

    }

    @Override
    protected String doInBackground(Void... params) {
//        if (params.length == 0) {
//            return null;
//        }
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJson = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            URL url = new URL(mBuiltUri.toString());

            Log.v(LOG_TAG, "Built URI " + mBuiltUri.toString());

            // Create the request to Movie DB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJson = buffer.toString();

            Log.v(LOG_TAG, "Forecast JSON String: " + movieJson.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return movieJson;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(aVoid);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        mFragmentCallback.onTaskDone(jsonObject);
    }
}

