package com.example.myfilmlist.presentation.views.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.integration.utils.PreviewListAdapter;
import com.example.myfilmlist.presentation.utils.InternetUtils;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements UpdatingView {

    private SearchView mSearchView;
    ProgressDialog progressDialog;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching...");
        mListView = findViewById(R.id.listView);
        mSearchView = findViewById(R.id.titleSearch);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if(InternetUtils.isNetworkAvailable(getApplicationContext())) {
                        progressDialog.show();
                        String data = mSearchView.getQuery().toString();
                        Presenter.getInstance().action(new Context(Events.SEARCH_BY_NAME, SearchActivity.this, data));
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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void update(Context resultData) {
        progressDialog.dismiss();
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
