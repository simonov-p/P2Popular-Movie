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
public class ReviewsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Movie mMovie;

    public ReviewsAdapter(Context context, Movie movie) {
        mMovie = movie;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMovie.reviews.size();
    }

    @Override
    public Movie.Review getItem(int position) {
        return mMovie.reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_review, parent, false);
        }
        Movie.Review review = mMovie.reviews.get(position);
        TextView textView = (TextView)convertView.findViewById(R.id.review_list_item_name_text_view);
        textView.setText(review.author);

        return convertView;
    }
}
