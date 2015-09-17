package com.example.petr.udacitypopularmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petr.udacitypopularmovies.R;
import com.example.petr.udacitypopularmovies.Utility;
import com.example.petr.udacitypopularmovies.api.MoviesAPI;
import com.example.petr.udacitypopularmovies.data.DBHelper;
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
    @Bind(R.id.detail_read)
    Button buttonRead;
    @Bind(R.id.detail_delete)
    Button buttonDelete;

    private Movie mMovie;
    private DBHelper mDBHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            int position = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
            mMovie = GridFragment.mAdapter.getItem(position);

            //db
            mDBHelper = new DBHelper(getContext());

            setMoreInfo();
            setReviewList();
            setTrailerList();

            title.setText(mMovie.title);
            Picasso.with(getActivity()).load(mMovie.getPosterUri()).into(poster);
            release.setText(Utility.getReleaseYear(mMovie.release_date));
            overview.setText(mMovie.overview);
            voteView.setText(String.format(getString(R.string.vote), mMovie.vote_average));

            buttonFavorite.setOnClickListener(myOnClick);
            buttonRead.setOnClickListener(myOnClick);
            buttonDelete.setOnClickListener(myOnClick);
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
                        for (Movie.Review review : mMovie.reviews) {
                            Log.e("mytag:review", review.toString());
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

                        for (Movie.Trailer trailer : mMovie.trailers) {
                            Log.e("mytag:trailer", trailer.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

private View.OnClickListener myOnClick = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String name = mMovie.title;
        String email = mMovie.overview;

        // подключаемся к БД
        SQLiteDatabase db = mDBHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.detail_favorite_button:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("title", name);
                cv.put("overview", email);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.detail_read:
                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("mytable", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("title");
                    int emailColIndex = c.getColumnIndex("overview");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", title = " + c.getString(nameColIndex) +
                                        ", overview = " + c.getString(emailColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                break;
            case R.id.detail_delete:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
        // закрываем подключение к БД
        mDBHelper.close();

    }
};


    private  void getTransparent(View view){
        view.setAlpha(view.getAlpha()/2);
    }
}
