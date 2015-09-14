package com.example.petr.udacitypopularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petr.udacitypopularmovies.R;
import com.example.petr.udacitypopularmovies.Utility;
import com.example.petr.udacitypopularmovies.api.FetchMovieTask;
import com.example.petr.udacitypopularmovies.objects.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by petr on 10.09.2015.
 */
public class DetailFragment extends Fragment {
    private Movie mMovie;

    @Bind(R.id.detail_duration_text_view) TextView durationView;
    @Bind(R.id.detail_vote_average_text_view) TextView voteView;
    @Bind(R.id.detail_title_text_view) TextView title;
    @Bind(R.id.detail_image_view) ImageView poster;
    @Bind(R.id.detail_release_text_view) TextView release;
    @Bind(R.id.detail_overview_text_view) TextView overview;
    @Bind(R.id.detail_favorite_button) Button buttonFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this,rootView);
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            int position = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
            mMovie = GridFragment.mAdapter.getItem(position);

            setMoreInfo();
            setTrailerList();
            setReviewList();

            title.setText(mMovie.title);
            Picasso.with(getActivity()).load(mMovie.poster_path).into(poster);
            release.setText(Utility.getReleaseYear(mMovie.release_date));
            overview.setText(mMovie.overview);
        }
        return rootView;
    }

    private void setMoreInfo() {
        FetchMovieTask fetchMovieTask2 = new FetchMovieTask(getContext(), mMovie,
                FetchMovieTask.REQUEST_MORE_INFO,
                new GridFragment.FragmentCallback() {
                    @Override
                    public void onTaskDone(JSONObject jsonObject) {
                        try {
                            durationView.setText(String.format(getString(R.string.runtime), jsonObject.getString("runtime")));
                            voteView.setText(String.format(getString(R.string.vote), jsonObject.getString("vote_average")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        fetchMovieTask2.execute();
    }

    private void  setReviewList() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask(getContext(), mMovie,
                FetchMovieTask.REQUEST_REVIEWS,
                new GridFragment.FragmentCallback() {
                    @Override
                    public void onTaskDone(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            ArrayList<String> author = new ArrayList<>();
                            ArrayList<String> content = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i ++) {
                                author.add(jsonArray.getJSONObject(i).getString("author"));
                                content.add(jsonArray.getJSONObject(i).getString("content"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        fetchMovieTask.execute();
    }

    private void setTrailerList() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask(getContext(), mMovie,
                FetchMovieTask.REQUEST_VIDEOS,
                new GridFragment.FragmentCallback() {
                    @Override
                    public void onTaskDone(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            ArrayList<String> keys = new ArrayList<>();
                            ArrayList<String> names = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i ++) {
                                keys.add(jsonArray.getJSONObject(i).getString("key"));
                                names.add(jsonArray.getJSONObject(i).getString("name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        fetchMovieTask.execute();
    }
}
