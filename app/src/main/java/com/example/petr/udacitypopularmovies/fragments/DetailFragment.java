package com.example.petr.udacitypopularmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
    @Bind(R.id.detail_trailers_list_view)
    ListView trailersListView;
    @Bind(R.id.detail_reviews_header)
    TextView reviewsHeader;
    @Bind(R.id.detail_reviews_list_view)
    ListView reviewsListView;

    private Movie mMovie;
    private MovieDbHelper mDBHelper;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;


    private View.OnClickListener mMarkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMovie.isFavorite) {
                removeFromDb();
                mMovie.isFavorite = false;
            } else {
                addToDb();
                mMovie.isFavorite = true;
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
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            int position = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
            mMovie = GridFragment.mAdapter.getItem(position);

            mDBHelper = new MovieDbHelper(getContext());

            setMoreInfo();
            setReviewList();
            setTrailerList();

            title.setText(mMovie.title);
            Picasso.with(getActivity()).load(mMovie.getPosterUri()).into(poster);
            release.setText(Utility.getReleaseYear(mMovie.release_date));
            overview.setText(mMovie.overview);
            voteView.setText(String.format(getString(R.string.vote), mMovie.vote_average));

            poster.setOnClickListener(imageOnClick);
            buttonFavorite.setOnClickListener(mMarkOnClickListener);
            setButtonType();

        }
        return rootView;
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
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            mReviewsAdapter = new ReviewsAdapter(getContext(),
                                    mMovie);
                            reviewsListView.setAdapter(mReviewsAdapter);

                            reviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle(mReviewsAdapter.getItem(position).author)
                                            .setMessage(mReviewsAdapter.getItem(position).content)
                                            .show();
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            mTrailersAdapter = new TrailersAdapter(getContext(),
                                    mMovie);
                            trailersListView.setAdapter(mTrailersAdapter);

                            trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://www.youtube.com/watch?v=" +
                                                    mTrailersAdapter.getItem(position).key)));
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private View.OnClickListener imageOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, mMovie.toString());
        }
    };
}
