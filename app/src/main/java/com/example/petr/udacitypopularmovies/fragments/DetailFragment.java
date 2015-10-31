package com.example.petr.udacitypopularmovies.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petr.udacitypopularmovies.R;
import com.example.petr.udacitypopularmovies.Utility;
import com.example.petr.udacitypopularmovies.api.MoviesAPI;
import com.example.petr.udacitypopularmovies.api.ReviewsAdapter;
import com.example.petr.udacitypopularmovies.api.TrailersAdapter;
import com.example.petr.udacitypopularmovies.data.MovieContract;
import com.example.petr.udacitypopularmovies.data.MovieDbHelper;
import com.example.petr.udacitypopularmovies.objects.Movie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by petr on 10.09.2015.
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = "mytag";
    public static final String DETAIL_POSITION = "position";
    @Bind(R.id.detail_duration_text_view)
    TextView durationView;
    @Bind(R.id.detail_vote_average_text_view)
    TextView voteView;
    @Bind(R.id.detail_title_text_view)
    TextView title;
    @Bind(R.id.detail_image_view)
    ImageView poster;
    @Bind(R.id.detail_release_text_view)
    TextView release;
    @Bind(R.id.detail_overview_text_view)
    TextView overview;
    @Bind(R.id.detail_favorite_button)
    Button buttonFavorite;
    @Bind(R.id.detail_trailers_header)
    TextView trailersHeader;
    @Bind(R.id.trailers_container)
    LinearLayout trailersContainer;
    @Bind(R.id.detail_reviews_header)
    TextView reviewsHeader;
    @Bind(R.id.reviews_container)
    LinearLayout reviewsContainer;

    private Movie mMovie;
    private MovieDbHelper mDBHelper;


    private View.OnClickListener mMarkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMovie.isFavorite) {
                mMovie.isFavorite = false;
                removeFromDb();
            } else {
                mMovie.isFavorite = true;
                addToDb();
            }
            setButtonType();
        }
    };

    private void removeFromDb(){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.execSQL("delete from " + MovieContract.MovieEntry.TABLE_NAME + " where " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + mMovie.id);
        db.close();
    }

    private void addToDb(){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues cv = mMovie.putMovieToCV();
        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
        db.close();
    }

    private boolean checkMovie(){
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String query = "SELECT  * FROM " + MovieContract.MovieEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int idColIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        if (cursor.moveToFirst()) {
            do {
                int db_id = cursor.getInt(idColIndex);
                if (db_id == mMovie.id){
                    mMovie.isFavorite = true;
                    return true;
                }
            } while (cursor.moveToNext());
        }
        mMovie.isFavorite = false;
        cursor.close();
        db.close();
        return false;
    }

    private void setButtonType(){
        if (checkMovie()) {
            buttonFavorite.setBackgroundColor(getResources().getColor(R.color.white));
            buttonFavorite.setText(getString(R.string.remove_from_favorite));
        } else {
            buttonFavorite.setBackgroundColor(getResources().getColor(R.color.detail_frg_button_background));
            buttonFavorite.setText(getString(R.string.mark_as_favorite));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView rootView = (ScrollView) inflater.inflate(R.layout.fragment_detail, container, false);
        rootView.fullScroll(View.FOCUS_UP);

        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        Bundle arguments = getArguments();

        if (GridFragment.mAdapter != null && (intent != null || arguments != null )) {
            int position;
            if (arguments != null) {
                position = arguments.getInt(DetailFragment.DETAIL_POSITION);
            } else {
                position = intent.getIntExtra(Intent.EXTRA_TEXT, 1);
            }
            mMovie = GridFragment.mAdapter.getItem(position);

            mDBHelper = GridFragment.mDBHelper;

            setMoreInfo();
            setReviewList();
            setTrailerList();

            title.setText(mMovie.title);
            Picasso.with(getActivity()).load(mMovie.getPosterUri()).into(poster);
            release.setText(Utility.getReleaseYear(mMovie.release_date));
            overview.setText(mMovie.overview);
            voteView.setText(String.format(getString(R.string.vote), mMovie.vote_average));

            buttonFavorite.setOnClickListener(mMarkOnClickListener);
            setButtonType();
            return rootView;

        } else {
            return null;
        }
    }

    private void setMoreInfo() {
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.the_movieDB_base_url))
                .build();

        MoviesAPI moviesAPI = adapter.create(MoviesAPI.class);

        moviesAPI.getMovieMoreInfo(mMovie.id,
                getString(R.string.the_movieDB_API_key),
                new Callback<Movie>() {
                    @Override
                    public void success(Movie movie, Response response) {
                        mMovie.runtime = movie.runtime;
                        durationView.setText(String.format(getString(R.string.runtime), mMovie.runtime));
                        durationView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });
    }

    private void setReviewList() {
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.the_movieDB_base_url))
                .build();

        MoviesAPI moviesAPI = adapter.create(MoviesAPI.class);

        moviesAPI.getMovieReviews(mMovie.id,
                getString(R.string.the_movieDB_API_key),
                new Callback<Movie.Reviews>() {
                    @Override
                    public void success(Movie.Reviews reviews, Response response) {
                        mMovie.reviews = reviews.results;
                        if (mMovie.reviews != null && mMovie.reviews.size() > 0) {
                            reviewsHeader.setVisibility(View.VISIBLE);
                            reviewsContainer.setVisibility(View.VISIBLE);
                            for (final Movie.Review review : mMovie.reviews){
                                TextView reviewTextView = new TextView(getContext());
                                reviewTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.detail_small_padding),
                                        getResources().getDimensionPixelSize(R.dimen.detail_small_padding),
                                        getResources().getDimensionPixelSize(R.dimen.detail_small_padding),
                                        getResources().getDimensionPixelSize(R.dimen.detail_small_padding));

                                reviewTextView.setText(review.author);
                                reviewTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new AlertDialog.Builder(getContext())
                                                .setTitle(review.author)
                                                .setMessage(review.content)
                                                .show().setCanceledOnTouchOutside(true);
                                    }
                                });
                                reviewsContainer.addView(reviewTextView);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), getString(R.string.error_download), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void setTrailerList() {
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.the_movieDB_base_url))
                .build();

        MoviesAPI moviesAPI = adapter.create(MoviesAPI.class);

        moviesAPI.getMovieTrailers(mMovie.id,
                getString(R.string.the_movieDB_API_key),
                new Callback<Movie.Trailers>() {
                    @Override
                    public void success(Movie.Trailers trailers, Response response) {
                        mMovie.trailers = trailers.results;
                        if (mMovie.trailers != null && mMovie.trailers.size() > 0) {
                            trailersHeader.setVisibility(View.VISIBLE);
                            trailersContainer.setVisibility(View.VISIBLE);
                            for (final Movie.Trailer trailer : mMovie.trailers){
                                View layout = getLayoutInflater(getArguments()).inflate(R.layout.list_item_trailer, null);
                                TextView trailerTextView = (TextView) layout.findViewById(R.id.trailer_list_item_text_view);
                                trailerTextView.setText(trailer.name);
                                layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(getString(R.string.youtube_base_name) +
                                                    trailer.key)));
                                    }
                                });
                                trailersContainer.addView(layout);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), getString(R.string.error_download), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
