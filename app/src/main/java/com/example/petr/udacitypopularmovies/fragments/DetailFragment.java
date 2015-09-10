package com.example.petr.udacitypopularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.petr.udacitypopularmovies.R;

/**
 * Created by petr on 10.09.2015.
 */
public class DetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            TextView title = (TextView) rootView.findViewById(R.id.detail_title_text_view);
            title.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }

        return rootView;
    }
}
