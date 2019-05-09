package com.example.myfilmlist.presentation.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.integration.utils.PreviewListAdapter;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends UpdatingView
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView filmsViewed;
    private ProgressDialog progressDialog;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Searching...");
        progressDialog.setInverseBackgroundForced(true);

        filmsViewed = findViewById(R.id.main_list_view);
        filmsViewed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                progressDialog.show();
                Thread taskThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            TFilmPreview filmFull = (TFilmPreview) filmsViewed.getAdapter().getItem(position); //We get the film we selected.
                            String imdbId = filmFull.getImdbID(); //Gets the IMDB id. We will use it to find the film with full information.
                            Presenter.getInstance().action(new Context(Events.SEARCH_BY_IMDB_ID, MainActivity.this, imdbId));
                        } catch (final ASException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    e.showMessage(MainActivity.this);
                                }
                            });
                        }
                    }
                });
                taskThread.start();
            }
        });

        emptyView = getLayoutInflater().inflate(R.layout.empty_layout, null);
        ViewGroup viewGroup = (ViewGroup) filmsViewed.getParent();
        viewGroup.addView(emptyView, viewGroup.getLayoutParams());
        filmsViewed.setEmptyView(emptyView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        try{
            Presenter.getInstance().action(new Context(Events.GET_ALL_VIEWED_FILMS, MainActivity.this, null));
        }
        catch (ASException ex) {
            ex.showMessage(MainActivity.this);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_review) {
            Intent reviewIntent = new Intent(this, ReviewActivity.class);
            startActivity(reviewIntent);
        } else if (id == R.id.nav_searchbyname) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void update(Context resultData) {

        if(resultData.getEvent() == Events.SEARCH_BY_IMDB_ID) {
            progressDialog.dismiss();
            Intent fullfilmIntent = new Intent(MainActivity.this, FullFilmActivity.class);
            fullfilmIntent.putExtra(SearchActivity.FULL_FILM_FROM_SEARCHVIEW, (TFilmFull)resultData.getData()); //We set the film into the intent
            startActivity(fullfilmIntent);
        }
        else if(resultData.getEvent() == Events.GET_ALL_VIEWED_FILMS) {
            PreviewListAdapter adapter = new PreviewListAdapter(this, R.layout.film_preview_layout,
                    new ArrayList<TFilmPreview>());
            if (resultData.getData() != null) {
                List<TFilmPreview> viewedFilms = (List<TFilmPreview>) resultData.getData();
                adapter.addAll(viewedFilms);
                emptyView.setVisibility(View.GONE);
                filmsViewed.setAdapter(adapter);

            } else {
                emptyView.setVisibility(View.VISIBLE);
                filmsViewed.setAdapter(adapter);
            }
        }
    }
}