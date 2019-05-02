package com.example.myfilmlist.presentation.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmFull;

public class FullFilmActivity extends AppCompatActivity {

    private WebView mWebView;
    private TFilmFull fullFilm;

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
                view.evaluateJavascript("loadFilm('"+fullFilm.getTitle()+"','"
                        +fullFilm.getYear()+"','"
                        +fullFilm.getRate()+"','"
                        +fullFilm.getImageURL()+"','"
                        +fullFilm.getDuration()+"','"
                        +fullFilm.getGenre()+"','"
                        +fullFilm.getDirectors()+"','"
                        +fullFilm.getActors()+"','"
                        +fullFilm.getPlot()
                        +"')", null);
            }
        });
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
}
