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

    public static void showErrorToast(Fragment fragment, RetrofitError retrofitError) {
        if (retrofitError.getMessage() != null) {
            Toast.makeText(fragment.getActivity(),
                    retrofitError.getMessage()
                    , Toast.LENGTH_SHORT).show();
            Log.e("mytag:getMessage", retrofitError.getMessage());
//            Log.e("mytag:getUrl", retrofitError.getUrl());
//            Log.e("mytag:getBody", retrofitError.getBody().toString());
//            Log.e("mytag:getCause", retrofitError.getCause().toString());
//            Log.e("mytag:getResponse", retrofitError.getResponse().toString());
            Log.e("mytag:getResponsegetStatus", retrofitError.getResponse().getStatus() + "");
            Log.e("mytag:getResponsegetReason", retrofitError.getResponse().getReason());
        }


    }
}
