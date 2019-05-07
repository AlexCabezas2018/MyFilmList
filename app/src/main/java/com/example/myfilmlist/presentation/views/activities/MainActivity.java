package com.example.myfilmlist.presentation.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.integration.daofilm.SQLiteHandlerViewedFilms;
import com.example.myfilmlist.integration.utils.PreviewListAdapter;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.List;

public class MainActivity extends UpdatingView
        implements NavigationView.OnNavigationItemSelectedListener {


    ListView filmsViewed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filmsViewed = findViewById(R.id.main_list_view);

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
            // Handle the camera action
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

        if(resultData.getData() != null) {
            List<TFilmPreview> viewedFilms = (List<TFilmPreview>) resultData.getData();
            PreviewListAdapter adapter = new PreviewListAdapter(MainActivity.this, R.layout.film_preview_layout,
                    viewedFilms);
            filmsViewed.setAdapter(adapter);
        }
        else {
            //TODO: Mejor forma de mostrar esto.
            Toast.makeText(getApplicationContext(), "NO FILMS!", Toast.LENGTH_SHORT).show();
        }
    }
}