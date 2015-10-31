package com.example.petr.udacitypopularmovies.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.petr.udacitypopularmovies.R;
import com.example.petr.udacitypopularmovies.objects.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by petr on 09.09.2015.
 */
public class MovieAdapter extends BaseAdapter {

    ArrayList<Movie> movies;
    Context mContext;
    LayoutInflater layoutInflater;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
        this.mContext = context;
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_grid_fragment, parent, false);
        }
        Movie movie = movies.get(position);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.list_item_image_view);
        final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progress_bar);

        Picasso.with(mContext).load(movie.getPosterUri()).
                placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).
                into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });


        return convertView;
    }
}
