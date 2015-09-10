package com.example.petr.udacitypopularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.petr.udacitypopularmovies.fragments.DetailFragment;

/**
 * Created by petr on 10.09.2015.
 */
public class DetailActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }
}
