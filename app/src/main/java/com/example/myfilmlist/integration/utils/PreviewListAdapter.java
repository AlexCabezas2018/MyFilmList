package com.example.myfilmlist.integration.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PreviewListAdapter extends ArrayAdapter<TFilmPreview> {

    private Context context;
    private int resource;
    private List<TFilmPreview> films;

    public PreviewListAdapter(Context context, int resource, List<TFilmPreview> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.films = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View previewView = inflater.inflate(resource, null);

        TextView title = previewView.findViewById(R.id.titlePreview);
        TextView type = previewView.findViewById(R.id.typePreview);
        TextView year = previewView.findViewById(R.id.yearPreview);
        ImageView poster = previewView.findViewById(R.id.posterPreview);

        TFilmPreview film = films.get(position);

        title.setText(film.getTitle());
        type.setText(film.getType());
        String prevYear = film.getYear();
        if (prevYear.endsWith("–")){
            prevYear = prevYear.concat("Present");
        }
        year.setText(prevYear);
        new ImageFromURL(poster).execute(film.getImageURL());

        return previewView;
    }
}
