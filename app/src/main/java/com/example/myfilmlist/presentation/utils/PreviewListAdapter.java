package com.example.myfilmlist.presentation.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.integration.utils.ImageFromURL;
import com.example.myfilmlist.presentation.views.UpdatingView;

import java.util.List;

public class PreviewListAdapter extends ArrayAdapter<TFilmPreview> {

    private UpdatingView view;
    private int resource;
    private List<TFilmPreview> films;
    private View previewView;

    public PreviewListAdapter(UpdatingView view, int resource, List<TFilmPreview> list) {
        super(view, resource, list);
        this.view = view;
        this.resource = resource;
        this.films = list;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(view);
        previewView = inflater.inflate(resource, null);

        TextView title = previewView.findViewById(R.id.titlePreview);
        TextView type = previewView.findViewById(R.id.typePreview);
        TextView year = previewView.findViewById(R.id.yearPreview);
        ImageView poster = previewView.findViewById(R.id.posterPreview);
        ImageButton button = previewView.findViewById(R.id.imageButtonAdd);

        final TFilmPreview actualFilm = films.get(position);

        title.setText(actualFilm.getTitle());
        type.setText(actualFilm.getType());
        String prevYear = actualFilm.getYear();
        if (prevYear.endsWith("–")){
            prevYear = prevYear.concat("Present");
        }
        year.setText(prevYear);
        new ImageFromURL(poster).execute(actualFilm.getImageURL());

        button.setOnClickListener(new ParameterOnClickListener(actualFilm, view) {});

        return previewView;
    }

}
