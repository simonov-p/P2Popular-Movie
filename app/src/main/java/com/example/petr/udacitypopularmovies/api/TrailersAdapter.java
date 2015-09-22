package com.example.petr.udacitypopularmovies.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.petr.udacitypopularmovies.R;
import com.example.petr.udacitypopularmovies.objects.Movie;

/**
 * Created by petr on 18.09.2015.
 */
public class TrailersAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Movie mMovie;
    public TrailersAdapter(Context context, Movie movie) {
        mMovie = movie;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMovie.trailers.size();
    }

    @Override
    public Movie.Trailer getItem(int position) {
        return mMovie.trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_trailer, parent, false);
        }
        Movie.Trailer trailer = mMovie.trailers.get(position);
        TextView textView = (TextView)convertView.findViewById(R.id.trailer_list_item_text_view);
        textView.setText(trailer.name);

        return convertView;
    }
}
