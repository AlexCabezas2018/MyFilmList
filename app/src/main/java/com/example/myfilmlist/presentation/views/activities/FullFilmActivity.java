package com.example.myfilmlist.presentation.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmFull;

public class FullFilmActivity extends AppCompatActivity {

    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_film);
        titleView = findViewById(R.id.test_film_tittle);
        TFilmFull film = (TFilmFull) getIntent().getSerializableExtra(SearchActivity.FULL_FILM_FROM_SEARCHVIEW);
        titleView.setText(film.getDuration());
    }
}
