package com.example.petr.udacitypopularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.petr.udacitypopularmovies.DetailActivity;
import com.example.petr.udacitypopularmovies.R;
import com.example.petr.udacitypopularmovies.api.MovieAdapter;
import com.example.petr.udacitypopularmovies.api.MoviesAPI;
import com.example.petr.udacitypopularmovies.objects.Movie;
import com.example.petr.udacitypopularmovies.objects.Movies;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class GridFragment extends Fragment {

    private static final String ENDPOINT = "http://api.themoviedb.org";
    String api_key = "faabcad1fcaddf30f757c38d94d44bc0";

    public static MovieAdapter mAdapter;
    @Bind(R.id.grid_view)
    GridView gridView;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private List<Movie> results;

    private String SORT_BY_VOTE = "vote_average";
    private String SORT_BY_POPULARITY = "popularity";
    private String mCurrentSort = SORT_BY_VOTE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, root);

        updateMovies();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, position);
                startActivity(intent);
            }
        });

        return root;
    }

    private void updateMovies() {
        if (mCurrentSort.equals(SORT_BY_VOTE)) {
            mCurrentSort = SORT_BY_POPULARITY;
        } else {
            mCurrentSort = SORT_BY_VOTE;
        }

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        MoviesAPI moviesAPI = adapter.create(MoviesAPI.class);

        moviesAPI.getMovies(mCurrentSort + ".desc", api_key, "1000",
                new Callback<Movies>() {
            @Override
            public void success(Movies movies, Response response) {
                mMovies = (ArrayList<Movie>) movies.results;
                mAdapter = new MovieAdapter(getContext(), mMovies);
                gridView.setAdapter(mAdapter);
                Log.e("mytag:getUrl", response.getUrl());

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                Log.e("mytag", error.toString());
            }
        });

        Toast.makeText(getContext(), "Sort order by " + mCurrentSort, Toast.LENGTH_SHORT).show();
        printMovies(mMovies);
    }

    void printMovies(ArrayList<Movie> movies) {
        Log.e("mytag:", movies.size() + "");

        for (Movie movie : movies) {
            Log.e("mytag:", movie.toString());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_resort) {
            updateMovies();
        }
        return true;
    }

    public interface FragmentCallback {
        void onTaskDone(String result);
    }
}
