package com.example.myfilmlist.integration.utils;

import com.example.myfilmlist.business.applicationservice.TFilmPreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilmUtils {
    public static List<TFilmPreview> getFilmsFromJSON(JSONObject jsonObject) throws JSONException {
        List<TFilmPreview> filmsToReturn = new ArrayList<>();

        JSONArray films = jsonObject.getJSONArray("Search");
        for (int i = 0; i < films.length(); i++) {
            TFilmPreview filmPreview = new TFilmPreview();
            JSONObject film = films.getJSONObject(i);

            filmPreview.setTitle(film.getString("Title"));
            filmPreview.setYear(film.getString("Year"));
            filmPreview.setImdbID(film.getString("imdbID"));
            filmPreview.setType(film.getString("Type"));
            filmPreview.setImageURL(film.getString("Poster"));

            filmsToReturn.add(filmPreview);
        }

        return filmsToReturn;
    }
}
