package com.example.myfilmlist.presentation.views.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daoreview.DAOReview;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

public class TestActiviy extends UpdatingView {

    EditText testEditText;
    Button buttonInsert;
    Button buttonGet;
    TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activiy);
        testEditText = findViewById(R.id.testeditext);
        buttonGet = findViewById(R.id.buttonGet);
        buttonInsert = findViewById(R.id.buttonInsert);
        contentTextView = findViewById(R.id.textView3);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TReview review = new TReview();
                review.setImdbId(testEditText.getText().toString());
                review.setContent("Hola, esto es una prueba para probar que se ha insertado bien.");
                try {
                    Presenter.getInstance().action(new Context(Events.ADD_REVIEW, TestActiviy.this, review));
                } catch (ASException e) {
                    e.showMessage(TestActiviy.this);
                }
            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = testEditText.getText().toString();
                if(text.equals("")) Toast.makeText(getApplicationContext(), "Introduce an id!", Toast.LENGTH_SHORT).show();
                else {
                    Integer imdbid = Integer.valueOf(text);
                    try {
                        Presenter.getInstance().action(new Context(Events.LOAD_REVIEW, TestActiviy.this, imdbid));

                    } catch (ASException e) {
                        e.showMessage(TestActiviy.this);
                    }
                }
            }
        });

    }

    @Override
    public void update(Context resultData) {
        if(resultData.getEvent() == Events.ADD_REVIEW) {
            boolean dataok = (Boolean) resultData.getData();
            if(dataok) Toast.makeText(getApplicationContext(), "TODO OK", Toast.LENGTH_LONG).show();
        }

        if(resultData.getEvent() == Events.LOAD_REVIEW) {
            TReview review = (TReview) resultData.getData();
            contentTextView.setText(review.getId() + " " + review.getImdbId() + " " + review.getContent() + "\n");
        }
    }
}

