package com.example.petr.udacitypopularmovies;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import retrofit.RetrofitError;

/**
 * Created by petr on 13.09.2015.
 */
public class Utility {
    public static String getReleaseYear(String release) {
        return release.substring(0,4);
    }

}
