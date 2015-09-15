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
import com.example.petr.udacitypopularmovies.api.FetchMovieTask;
import com.example.petr.udacitypopularmovies.api.MovieAdapter;
import com.example.petr.udacitypopularmovies.objects.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class GridFragment extends Fragment {

    @Bind(R.id.grid_view)GridView gridView;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    public static MovieAdapter mAdapter;
    private static final String ENDPOINT = "http://api.themoviedb.org/3";

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

        ButterKnife.bind(this,root);

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

    private ArrayList<Movie> parseJson(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray("results");
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            JsonObject gobject= (JsonObject) new JsonParser().parse(object.toString());
            Gson gson = new Gson();
            Movie m = gson.fromJson(gobject,Movie.class);
            m.poster_path = m.getPosterUri(m.poster_path);

            Log.e("mytag,gobject", gobject.toString());
            Log.e("mytag,m", m.toString());
            movies.add(m);
        }
        return movies;
    }

    private void updateMovies(){
        if (mCurrentSort.equals(SORT_BY_VOTE)) {
            mCurrentSort = SORT_BY_POPULARITY;
        } else {
            mCurrentSort = SORT_BY_VOTE;
        }






//        String url = "http://api.themoviedb.org/3";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .build();
//
//        MoviesAPI api = retrofit.create(MoviesAPI.class);
//
//        Call<List<Movie>> repo = api.listMovie("faabcad1fcaddf30f757c38d94d44bc0");
//
//        Log.e("mytag", results.toString());
        FetchMovieTask fetchMovieTask = new FetchMovieTask(getContext(), mCurrentSort, new FragmentCallback() {
            @Override
            public void onTaskDone(JSONObject jsonObject) {
                try {
                    mMovies = parseJson(jsonObject);
                    printMovies(mMovies);
                    mAdapter = new MovieAdapter(getContext(),mMovies);
                    gridView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        fetchMovieTask.execute();
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
        if (item.getItemId() == R.id.action_resort){
            updateMovies();
        }
        return true;
    }

    public interface FragmentCallback {
        public void onTaskDone(JSONObject jsonObject);
    }
}
