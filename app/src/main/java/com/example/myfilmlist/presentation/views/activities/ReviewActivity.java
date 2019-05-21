package com.example.myfilmlist.presentation.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends UpdatingView {

    public static final String REVIEW_DONE = "RD";

    private TextView title;
    private Button reviewButton;
    private EditText reviewText;
    private Spinner viewedSelect;
    private String imbdid = null;

    private List<String> viewedImbdids;
    private List<String> viewedTitles;
    private Button goSeachButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        imbdid = (String) getIntent().getSerializableExtra(FullFilmActivity.FILM_ID);
        title =  findViewById(R.id.review_title);

        reviewText = findViewById(R.id.review_text);
        reviewText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        reviewText.requestFocus();

        reviewButton = findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = isValidReview(reviewText.getText().toString());
                if (!error.equals("")) {
                    reviewText.setError(error);
                } else {
                    try {
                        TReview review = new TReview();
                        review.setContent(reviewText.getText().toString());
                        review.setImdbId(imbdid);
                        Presenter.getInstance().action(new Context(Events.ADD_REVIEW, ReviewActivity.this, review));

                    }
                    catch (ASException exception) {
                        exception.showMessage(ReviewActivity.this);
                    }
                }
            }
        });

        goSeachButton = findViewById(R.id.go_search_button);
        goSeachButton.setVisibility(View.GONE);
        goSeachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(ReviewActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                finish();
            }
        });

        viewedSelect = findViewById(R.id.viewed_select);

        if (imbdid != null){
            viewedSelect.setVisibility(View.GONE);
            title.setText("Write a review for " + ((String) getIntent().getSerializableExtra(FullFilmActivity.FILM_TITLE)));
        } else {
            try {
                Presenter.getInstance().action(new Context(Events.GET_ALL_VIEWED_FILMS, ReviewActivity.this, null));
            } catch (ASException ex) {
                ex.showMessage(ReviewActivity.this);
            }
            viewedSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    reviewText.setError(null);
                    if (position > 0) {
                        title.setText("Write a review for " + (String) parent.getItemAtPosition(position));
                        imbdid = viewedImbdids.get(position-1);
                    } else {
                        title.setText("Select a film to review!");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    title.setText("Select a film to review!");
                }
            });
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private String isValidReview(String review){
        String valid = "";
        if (viewedSelect.getSelectedItemPosition() == 0){
            valid = "The film can't be empty";
        } else if (review.length() <= 0){
            valid = "The review can't be empty";
        }
        return valid;
    }

    @Override
    public void update(Context resultData) {
        Events event = resultData.getEvent();
        if(event == Events.ADD_REVIEW) {
            if((boolean) resultData.getData()) {
                Toast.makeText(getApplicationContext(), "Review added successfully!", Toast.LENGTH_SHORT).show();
                Intent result = new Intent();
                result.putExtra(REVIEW_DONE, true);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        }

        if(event == Events.GET_ALL_VIEWED_FILMS){
            viewedTitles = new ArrayList<>();
            viewedImbdids = new ArrayList<>();
            if(resultData.getData() != null) {
                List<TFilmPreview> viewedFilms = (List<TFilmPreview>) resultData.getData();
                viewedTitles.add("-");
                for (TFilmPreview film : viewedFilms) {
                    viewedTitles.add(film.getTitle());
                    viewedImbdids.add(film.getImdbID());
                }
            }
            else {
                title.setText("No films viewed!");
                viewedSelect.setVisibility(View.GONE);
                reviewButton.setVisibility(View.GONE);
                reviewText.setVisibility(View.GONE);
                goSeachButton.setVisibility(View.VISIBLE);
            }
            viewedSelect.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, viewedTitles));
        }
    }

}
