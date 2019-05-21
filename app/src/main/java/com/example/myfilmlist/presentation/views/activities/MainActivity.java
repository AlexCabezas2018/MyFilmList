package com.example.myfilmlist.presentation.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.utils.BackListener;
import com.example.myfilmlist.presentation.utils.PreviewRecyclerAdapter;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends UpdatingView
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog;
    private View emptyView;

    private RecyclerView recyclerView;
    private List<TFilmPreview> viewedFilms;
    private PreviewRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Searching...");
        progressDialog.setInverseBackgroundForced(true);

        recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyView = getLayoutInflater().inflate(R.layout.empty_layout, null);
        ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        viewGroup.addView(emptyView, viewGroup.getLayoutParams());

        viewedFilms = new ArrayList<>();

        recyclerAdapter = new PreviewRecyclerAdapter(viewedFilms);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerAdapter.setBackListener(new BackListener() {
            @Override
            public void onBackClicked(int position) {
                TFilmPreview preview = viewedFilms.get(position);
                try {
                    Presenter.getInstance().action(new Context(Events.SEARCH_BY_IMDB_ID, MainActivity.this, preview.getImdbID()));
                } catch (ASException e) {
                    e.showMessage(MainActivity.this);
                }
            }
        });


        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            ColorDrawable redbackground = new ColorDrawable(Color.parseColor("#FF0000"));
            Drawable deleteIcon = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_delete);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                try{
                    Presenter.getInstance().action(new Context(Events.REMOVE_FROM_FILMLIST, MainActivity.this,
                            new Pair<>(viewedFilms.get(viewHolder.getAdapterPosition()), viewHolder)));
                }
                catch (ASException ex) {
                    ex.showMessage(MainActivity.this);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                redbackground.setBounds((itemView.getRight() + (int)dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                deleteIcon.setBounds(
                        itemView.getRight() - iconMargin - deleteIcon.getIntrinsicHeight(),
                        itemView.getTop() + iconMargin,
                        itemView.getRight() - iconMargin,
                        itemView.getBottom() - iconMargin);
                redbackground.draw(c);
                c.save();
                c.clipRect((itemView.getRight() + (int)dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                deleteIcon.draw(c);
                c.restore();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemHelper = new ItemTouchHelper(callback);
        itemHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);

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
        } else if (id == R.id.nav_send){
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
            if (resultData.getData() != null) {
                viewedFilms.clear();
                viewedFilms.addAll((List<TFilmPreview>) resultData.getData());
                recyclerAdapter.notifyDataSetChanged();
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } else if(resultData.getEvent() == Events.REMOVE_FROM_FILMLIST){
            RecyclerView.ViewHolder data = (RecyclerView.ViewHolder) resultData.getData();
            viewedFilms.remove(data.getAdapterPosition());
            recyclerAdapter.notifyDataSetChanged();
            if(viewedFilms.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            Toast.makeText(getApplicationContext(), "Film removed successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}