package com.example.petr.udacitypopularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petr.udacitypopularmovies.R;
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
            int position = intent.getIntExtra(GridFragment.EXTRA,-1);
            Movie movie = GridFragment.mAdapter.getItem(position);
            Log.e("mytag3", movie.toString());

            TextView title = (TextView) rootView.findViewById(R.id.detail_title_text_view);
            TextView overview = (TextView) rootView.findViewById(R.id.detail_description_text_view);
            ImageView poster = (ImageView) rootView.findViewById(R.id.detail_image_view);

            title.setText(movie.title);
            overview.setText(movie.overview);
            Picasso.with(getActivity()).load(movie.poster_path).into(poster);
        }

        return rootView;
    }
}
