package com.example.myfilmlist.presentation.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.integration.utils.PreviewListAdapter;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements UpdatingView {

    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mListView = findViewById(R.id.listView);
        mSearchView = findViewById(R.id.titleSearch);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    String data = mSearchView.getQuery().toString();
                    Presenter.getInstance().action(new Context(Events.SEARCH_BY_NAME, SearchActivity.this, data));
                }
                catch (ASException ex){
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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void update(Context resultData) {
        PreviewListAdapter adapter = new PreviewListAdapter(this, R.layout.film_preview_layout, (List<TFilmPreview>) resultData.getData() );
        mListView.setAdapter(adapter);
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
