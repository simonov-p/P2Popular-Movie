package com.example.petr.udacitypopularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.petr.udacitypopularmovies.data.MovieContract;
import com.example.petr.udacitypopularmovies.data.MovieDbHelper;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_resort) {
            Toast.makeText(this, "resort", Toast.LENGTH_SHORT).show();
            GridFragment.movies = MovieDbHelper.getMovies(MovieContract.MovieEntry.COLUMN_POPULARITY);
            GridFragment.mAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_resort2) {
            Toast.makeText(this, "resort", Toast.LENGTH_SHORT).show();
            GridFragment.FetchMovieTask fetchMovieTask = new GridFragment.FetchMovieTask();
            fetchMovieTask.execute(GridFragment.SORT_BY_VOTE);
            GridFragment.movies = MovieDbHelper.getMovies(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);

            GridFragment.mAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
