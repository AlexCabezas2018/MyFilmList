package com.example.myfilmlist.presentation.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

public class FullFilmActivity extends UpdatingView {

    public static final String FILM_TITLE = "FTR";
    public static final String FILM_ID = "FID";
    public static final int REVIEW = 1;

    private WebView mWebView;
    private TFilmFull fullFilm;

    private Boolean reviewAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_film);
        mWebView = findViewById(R.id.webView);
        fullFilm = (TFilmFull) getIntent().getSerializableExtra(SearchActivity.FULL_FILM_FROM_SEARCHVIEW);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished (WebView view, String url){
                String script = "loadFilm('"+
                        fullFilm.getTitle().replace("'", "´")+"', '" +
                        fullFilm.getYear()+"', '" +
                        fullFilm.getRate()+"', '"+
                        fullFilm.getImageURL()+"', '" +
                        fullFilm.getDuration()+"', '"+
                        fullFilm.getGenre().replace("'", "´")+"', '"+
                        fullFilm.getDirectors().replace("'", "´")+"', '"+
                        fullFilm.getActors().replace("'", "´")+"', '"+
                        fullFilm.getPlot().replace("'", "´")+"', '"+
                        fullFilm.getType()+"', '"+
                        fullFilm.getImbdid()+"')";
                view.evaluateJavascript(script, null);
                try {
                    Presenter.getInstance().action(new Context(Events.LOAD_REVIEW, FullFilmActivity.this, fullFilm.getImbdid()));
                } catch (ASException e) {
                    //e.showMessage(FullFilmActivity.this);
                }

            }
        });
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "interface");
        mWebView.loadUrl("file:///android_asset/html/index.html");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        //If the user just add a review, we search it in the database and update the view.
        if (reviewAdded){
            try {
                Presenter.getInstance().action(new Context(Events.LOAD_REVIEW, FullFilmActivity.this, fullFilm.getImbdid()));
            } catch (ASException e) {
                e.showMessage(FullFilmActivity.this);
            }
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(mWebView != null){
            mWebView.destroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REVIEW) : {
                if (resultCode == Activity.RESULT_OK) {
                    reviewAdded = data.getBooleanExtra(ReviewActivity.REVIEW_DONE, false);
                }
                break;
            }
        }
    }

    private class JavaScriptInterface {

        @JavascriptInterface
        public void addReviewFromJS(String title, String imdbid){
            Intent reviewIntent = new Intent(FullFilmActivity.this, ReviewActivity.class);
            reviewIntent.putExtra(FILM_TITLE, title);
            reviewIntent.putExtra(FILM_ID, imdbid);
            startActivityForResult(reviewIntent, REVIEW);
        }

        @JavascriptInterface
        public void addToViewsFromJS(String title, String year, String filmID, String type, String imageURL){
            /*TODO: Mucho ojo aquí. Por alguna razón la url en este método es " ", cuando he comprobado que cuando accede a la actividad, el campo de la url no está vacio. Por esa razón, la lista de vista no muestra correctamente las imágenes.*/

            try {
                TFilmPreview filmPreview = new TFilmPreview(title, year, filmID, type, imageURL);
                Presenter.getInstance().action(new Context(Events.ADD_TO_FILMLIST, FullFilmActivity.this, filmPreview));
            } catch (ASException ex) {
                ex.showMessage(FullFilmActivity.this);
            }
        }
    }


    @Override
    public void update(Context resultData) {
        Events event = resultData.getEvent();
        if(event == Events.LOAD_REVIEW) {
            if(resultData.getData() != null) {
                TReview tReview = (TReview) resultData.getData();
                mWebView.evaluateJavascript("loadReview('"+tReview.getContent().replace("'", "´")+"')", null);
            }
        }
        else if(event == Events.ADD_TO_FILMLIST) {
            Toast.makeText(getApplicationContext(), "Film added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}


