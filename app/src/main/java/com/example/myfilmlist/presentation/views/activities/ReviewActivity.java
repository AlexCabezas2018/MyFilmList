package com.example.myfilmlist.presentation.views.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

public class ReviewActivity extends UpdatingView {

    private TextView title;
    private Button reviewButton;
    private EditText reviewText;
    private String imbdid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //TODO: HACERLA GENÉRICA

        imbdid = (String) getIntent().getSerializableExtra(FullFilmActivity.FILM_ID);
        title =  findViewById(R.id.review_title);
        title.setText("Write a review for " + ((String) getIntent().getSerializableExtra(FullFilmActivity.FILM_TITLE)));

        reviewText = findViewById(R.id.review_text);
        reviewText.requestFocus();

        reviewButton = findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidReview(reviewText.getText().toString())) {
                    reviewText.setError("The review can't be empty");
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

    private boolean isValidReview(String review){
        boolean valid = false;
        if (review.length() > 0){
            valid = true;
        }
        return valid;
    }

    @Override
    public void update(Context resultData) {
        if(resultData.getEvent() == Events.ADD_REVIEW) {
            if((boolean) resultData.getData()) {
                Toast.makeText(getApplicationContext(), "Review added successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
