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
import com.example.petr.udacitypopularmovies.objects.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by petr on 10.09.2015.
 */
public class DetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            int position = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
            Movie movie = GridFragment.mAdapter.getItem(position);

            TextView title = (TextView) rootView.findViewById(R.id.detail_title_text_view);
            ImageView poster = (ImageView) rootView.findViewById(R.id.detail_image_view);
            TextView release = (TextView) rootView.findViewById(R.id.detail_release_text_view);
            TextView duration = (TextView) rootView.findViewById(R.id.detail_duration_text_view);
            TextView vote = (TextView) rootView.findViewById(R.id.detail_vote_average_text_view);
            Button buttonFavorite = (Button) rootView.findViewById(R.id.detail_favorite_button);
            TextView overview = (TextView) rootView.findViewById(R.id.detail_overview_text_view);

            title.setText(movie.title);
            Picasso.with(getActivity()).load(movie.poster_path).into(poster);
            release.setText(Utility.getReleaseYear(movie.release_date));
            duration.setText("no dur");
            vote.setText(String.format(getString(R.string.vote), movie.vote_average));
            overview.setText(movie.overview);
        }

        return rootView;
    }
}
