package com.example.myfilmlist.presentation.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.integration.utils.PreviewListAdapter;
import com.example.myfilmlist.integration.utils.InternetUtils;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.List;

public class SearchActivity extends UpdatingView {

    public static final String FULL_FILM_FROM_SEARCHVIEW = "FFFSV";
    public static final String IMBD_ID_FROM_SEARCHVIEW = "IMDBIDFSV";

    private SearchView mSearchView;
    private ProgressDialog progressDialog;
    private ListView mListView;
    private int currentPage;
    private String bufferedTitle;
    private TextView numPages;
    private Boolean moreResults;
    private String imdbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        currentPage = 1;
        moreResults = true;
        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Searching...");
        progressDialog.setInverseBackgroundForced(true);
        numPages = findViewById(R.id.numPages);
        mListView = findViewById(R.id.listView);
        mSearchView = findViewById(R.id.titleSearch);
        mSearchView.requestFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if(InternetUtils.isNetworkAvailable(getApplicationContext())) {
                        final String data = mSearchView.getQuery().toString();
                        if(bufferedTitle == null || !bufferedTitle.equals(data)) {
                            bufferedTitle = data;
                            currentPage = 1;
                            mSearchView.clearFocus();
                            progressDialog.show();
                            /* We execute complex process in other thread*/
                            Thread taskThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        Pair<String, Integer> pairToSearch = new Pair<>(data, currentPage);
                                        Presenter.getInstance().action(new Context(Events.SEARCH_BY_NAME_AND_PAGE, SearchActivity.this, pairToSearch));
                                    } catch (final ASException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                e.showMessage(SearchActivity.this);
                                            }
                                        });
                                    }
                                }
                            });
                            taskThread.start();

                        }
                    }
                    else throw new ASException("There was a problem while trying to connect to internet");
                }
                catch (ASException ex){
                    progressDialog.dismiss();
                    ex.showMessage(SearchActivity.this);
                    return false;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        /*This method will be called each time we select something from the listView*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                progressDialog.show();
                Thread taskThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            TFilmPreview filmFull = (TFilmPreview) mListView.getAdapter().getItem(position); //We get the film we selected.
                            imdbId = filmFull.getImdbID(); //Gets the IMDB id. We will use it to find the film with full information.
                            Presenter.getInstance().action(new Context(Events.SEARCH_BY_IMDB_ID, SearchActivity.this, imdbId));
                        } catch (final ASException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    e.showMessage(SearchActivity.this);
                                }
                            });
                        }
                    }
                });
                taskThread.start();
            }
        });

        /* This method will be called each time we scroll up or scroll down the scroll bar of the listView*/
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /*This if statement checks if we have reached the bottom of the listView*/
                if(!view.canScrollList(View.SCROLL_AXIS_VERTICAL) && scrollState == SCROLL_STATE_IDLE && moreResults) {
                    try {
                        /*We request more results*/
                        Presenter.getInstance().action(new Context(Events.SEARCH_BY_NAME_AND_PAGE,
                                SearchActivity.this, new Pair<>(bufferedTitle, currentPage)));
                    }
                    catch (ASException ex) {
                        ex.showMessage(SearchActivity.this);
                        if (ex.getMessage().contains("No more results for")){
                            moreResults = false;
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void update(final Context resultData) {
        /* Because we touch ui component, and we are executing tasks from NON-UI threads, we need to add this logic to the UIThread.*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Events event = resultData.getEvent();

                /* We differentiate all possible events of update*/

                if(event == Events.SEARCH_BY_NAME_AND_PAGE) {
                    Pair<List<TFilmPreview>, String> data = (Pair<List<TFilmPreview>, String>) resultData.getData();
                    if(currentPage == 1) { //Which means that is a new searching result
                        PreviewListAdapter adapter = new PreviewListAdapter(SearchActivity.this, R.layout.film_preview_layout,
                                data.first);
                        mListView.setAdapter(adapter);
                        setTitle("Results for " + '"' + bufferedTitle + '"'); //Changes the title (for example: results for "Batman" )
                        moreResults = true;
                    }
                    else { //We requested a new page
                        ((PreviewListAdapter) mListView.getAdapter()).
                                addAll(data.first);
                    }

                    /*For debug and testing*/

                    numPages.setText("Loaded " + currentPage + (currentPage == 1 ? " page" : " pages") + " of " + data.second);

                    currentPage++;
                }

                if(event == Events.SEARCH_BY_IMDB_ID){
                    Intent fullfilmIntent = new Intent(SearchActivity.this, FullFilmActivity.class);
                    fullfilmIntent.putExtra(FULL_FILM_FROM_SEARCHVIEW, (TFilmFull)resultData.getData()); //We set the film into the intent
                    startActivity(fullfilmIntent);
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
