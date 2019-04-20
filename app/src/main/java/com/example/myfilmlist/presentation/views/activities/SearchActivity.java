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
import android.widget.ListView;
import android.widget.SearchView;
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

public class SearchActivity extends AppCompatActivity implements UpdatingView {

    public static final String FULL_FILM_FROM_SEARCHVIEW = "FFFSV";

    private SearchView mSearchView;
    private ProgressDialog progressDialog;
    private ListView mListView;
    private int currentPage;
    private String bufferedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        currentPage = 1;
        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching...");
        mListView = findViewById(R.id.listView);
        mSearchView = findViewById(R.id.titleSearch);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if(InternetUtils.isNetworkAvailable(getApplicationContext())) {
                        String data = mSearchView.getQuery().toString();
                        if(bufferedTitle == null || !bufferedTitle.equals(data)) {
                            bufferedTitle = data;
                            currentPage = 1;
                            progressDialog.show();
                            Pair<String, Integer> pairToSearch = new Pair<>(data, currentPage);
                            Presenter.getInstance().action(new Context(Events.SEARCH_BY_NAME_AND_PAGE, SearchActivity.this, pairToSearch));
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    progressDialog.show();
                    TFilmPreview filmFull = (TFilmPreview) mListView.getAdapter().getItem(position); //We get the film we selected.
                    String imdbId = filmFull.getImdbID(); //Gets the IMDB id. We will use it to find the film with full information.
                    Presenter.getInstance().action(new Context(Events.SEARCH_BY_IMDB_ID, SearchActivity.this, imdbId));
                }
                catch (ASException ex) {
                    progressDialog.dismiss();
                    ex.showMessage(SearchActivity.this);
                }
            }
        });

        /* This method will be called each time we scroll up or scroll down the scroll bar of the listView*/
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /*This if statement checks if we have reached the bottom of the listView*/
                if(!view.canScrollList(View.SCROLL_AXIS_VERTICAL) && scrollState == SCROLL_STATE_IDLE) {
                    try {
                        /*We request more results*/
                        Presenter.getInstance().action(new Context(Events.SEARCH_BY_NAME_AND_PAGE,
                                SearchActivity.this, new Pair<>(bufferedTitle, currentPage)));
                    }
                    catch (ASException ex) {
                        ex.showMessage(SearchActivity.this);
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
    public void update(Context resultData) {
        progressDialog.dismiss();
        Events event = resultData.getEvent();
        Pair<List<TFilmPreview>, String> data = (Pair<List<TFilmPreview>, String>) resultData.getData();

        /* We differentiate all possible events of update*/

        if(event == Events.SEARCH_BY_NAME_AND_PAGE) {
            if(currentPage == 1) { //Which means that is a new searching result
                PreviewListAdapter adapter = new PreviewListAdapter(this, R.layout.film_preview_layout,
                        data.first);
                mListView.setAdapter(adapter);
                setTitle("Results for " + '"' + bufferedTitle + '"'); //Changes the title (for example: results for "Batman" )

            }
            else { //We requested a new page
                ((PreviewListAdapter) mListView.getAdapter()).
                        addAll(data.first);

            }

            /*For debug and testing*/
            /*TODO find a better way to show this information (instead of just 'toasting' it)*/
            Toast.makeText(getApplicationContext(),
                    "Page " + currentPage + " of " + data.second, Toast.LENGTH_LONG).show();

            currentPage++;
        }

        if(event == Events.SEARCH_BY_IMDB_ID){
            Intent fullfilmIntent = new Intent(SearchActivity.this, FullFilmActivity.class);
            fullfilmIntent.putExtra(FULL_FILM_FROM_SEARCHVIEW, (TFilmFull)resultData.getData()); //We set the film into the intent
            startActivity(fullfilmIntent);
        }
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
