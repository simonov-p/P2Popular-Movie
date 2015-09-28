package com.example.petr.udacitypopularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.petr.udacitypopularmovies.fragments.DetailFragment;

public class MainActivity extends AppCompatActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, new DetailFragment(),
                                DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else mTwoPane = false;
    }

    public static boolean isTwoPane() {
        return mTwoPane;
    }
}
