package com.example.myfilmlist.business.film.filmappservice;

import android.util.Pair;

import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

import java.util.List;

/**
 * This class is used to implement all the logic of the objects that we want to interact with
 */

public abstract class ASFilm {
    private static ASFilm instace = null;

    /*  SINGLETON  */
    public static ASFilm getInstance() {
        if(instace == null) {
            instace = new ASFilmImp();
        }
        return instace;
    }

    public abstract TFilmFull searchByIMDBId(String id) throws ASException;
    public abstract Pair<List<TFilmPreview>, String> searchByPage(Pair<String, Integer> nameAndPage) throws ASException;
    public abstract List<TFilmPreview> getAllViewedFilms(Context inputData) throws ASException;
}
