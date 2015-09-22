package com.example.petr.udacitypopularmovies.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.example.petr.udacitypopularmovies.Utility;
import com.example.petr.udacitypopularmovies.api.MovieAdapter;
import com.example.petr.udacitypopularmovies.api.MoviesAPI;
import com.example.petr.udacitypopularmovies.objects.Movie;
import com.example.petr.udacitypopularmovies.objects.Movies;

import java.util.ArrayList;

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

    private String SORT_BY_VOTE = "vote";
//    private String SORT_BY_VOTE = "vote_average";
    private String SORT_BY_POPULARITY = "popularity";
    private String SORT_BY_FAVORITES = "favorites";

    private String[] sortType = {SORT_BY_POPULARITY, SORT_BY_VOTE, SORT_BY_FAVORITES};

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
        String sortQuery = null;
        if (!mCurrentSort.equals(SORT_BY_FAVORITES)){

            sortQuery = mCurrentSort.equals(SORT_BY_VOTE) ? "vote_average" : "popularity";
            final RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(getString(R.string.the_movieDB_base_url))
                    .build();

            MoviesAPI moviesAPI = adapter.create(MoviesAPI.class);

            moviesAPI.getMovies(sortQuery + ".desc", getString(R.string.the_movieDB_API_key), "1000",
                    new Callback<Movies>() {
                        @Override
                        public void success(Movies movies, Response response) {
                            mMovies = (ArrayList<Movie>) movies.results;
                            mAdapter = new MovieAdapter(getContext(), mMovies);
                            gridView.setAdapter(mAdapter);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Utility.showErrorToast(GridFragment.this,error);
                        }
                    });
        } else {

        }
    }

//    void printMovies(ArrayList<Movie> movies) {
//        Log.e("mytag:", movies.size() + "");
//
//        for (Movie movie : movies) {
//            Log.e("mytag:", movie.toString());
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_resort) {

            new AlertDialog.Builder(getContext()).
                    setTitle("Sort by").
                    setItems(sortType, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurrentSort = sortType[which];
                            Toast.makeText(getContext(),mCurrentSort, Toast.LENGTH_SHORT).show();
                            updateMovies();
                        }
                    }).
                    show();

        }
        return true;
    }

}
