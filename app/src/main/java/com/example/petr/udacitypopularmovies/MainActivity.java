package com.example.petr.udacitypopularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.petr.udacitypopularmovies.fragments.GridFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GridFragment())
                    .commit();
        }
    }
}
