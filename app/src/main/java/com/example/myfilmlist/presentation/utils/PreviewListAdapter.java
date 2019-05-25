package com.example.myfilmlist.presentation.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.List;

public class PreviewListAdapter extends ArrayAdapter<TFilmPreview> {

    private UpdatingView view;
    private int resource;
    private List<TFilmPreview> films;

    public PreviewListAdapter(UpdatingView view, int resource, List<TFilmPreview> list) {
        super(view, resource, list);
        this.view = view;
        this.resource = resource;
        this.films = list;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(view);
        View previewView = inflater.inflate(resource, null);

        TextView title = previewView.findViewById(R.id.titlePreview);
        TextView type = previewView.findViewById(R.id.typePreview);
        TextView year = previewView.findViewById(R.id.yearPreview);
        ImageView poster = previewView.findViewById(R.id.posterPreview);
        ImageButton button = previewView.findViewById(R.id.imageButtonAdd);

        final TFilmPreview actualFilm = films.get(position);

        title.setText(actualFilm.getTitle());
        type.setText(actualFilm.getType());
        year.setText(actualFilm.getYear());

        Glide.with(view)
                .load(actualFilm.getImageURL())
                .transforms(new RoundedCorners(10))
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(poster);

        button.setOnClickListener(new ParameterOnClickListener(actualFilm, view) {});


        return previewView;
    }

}
