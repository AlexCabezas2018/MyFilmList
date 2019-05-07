package com.example.myfilmlist.presentation.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmFull;

public class FullFilmActivity extends AppCompatActivity {

    public static final String FILM_TITLE = "FTR";
    public static final String FILM_ID = "FID";

    private WebView mWebView;
    private TFilmFull fullFilm;
    private String imdbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_film);
        imdbid = (String) getIntent().getSerializableExtra(SearchActivity.IMBD_ID_FROM_SEARCHVIEW);
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
                        fullFilm.getPlot().replace("'", "´")+"')";
                view.evaluateJavascript(script, null);
            }
        });
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "interface");
        mWebView.loadUrl("file:///android_asset/html/index.html");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private class JavaScriptInterface {

        @JavascriptInterface
        public void addReviewFromJS(String title){
            Intent reviewIntent = new Intent(FullFilmActivity.this, ReviewActivity.class);
            reviewIntent.putExtra(FILM_TITLE, title);
            reviewIntent.putExtra(FILM_ID, imdbid);
            startActivity(reviewIntent);
        }

        @JavascriptInterface
        public void addToViewsFromJS(){
            Intent reviewIntent = new Intent(FullFilmActivity.this, FullFilmActivity.class);
            startActivity(reviewIntent);
        }
    }
}


