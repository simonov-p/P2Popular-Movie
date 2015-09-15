package com.example.petr.udacitypopularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.petr.udacitypopularmovies.data.MovieDbHelper;
import com.example.petr.udacitypopularmovies.fragments.GridFragment;

public class MainActivity extends AppCompatActivity {
    public static MovieDbHelper sMovieDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sMovieDbHelper = new MovieDbHelper(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GridFragment())
                    .commit();
        }

    }
}
