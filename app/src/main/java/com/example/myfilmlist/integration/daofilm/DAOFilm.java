package com.example.myfilmlist.integration.daofilm;

import android.app.Activity;
import android.util.Pair;

import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.presentation.context.Context;

import java.util.List;

/**
 * This class is used to access to the api and obtain the data of the films
 */

public abstract class DAOFilm {

    /*  SINGLETON  */
    private static DAOFilm instance = null;

    public static DAOFilm getInstance() {
        if(instance == null) {
            instance = new DAOFilmImp();
        }
        return instance;
    }

    public abstract TFilmFull getFilmByIMDBId(String id) throws DAOException;
    public abstract Pair<List<TFilmPreview>, String> getFilmsFromNextPage(Pair<String, Integer> inputInformation) throws DAOException;
    public abstract List<TFilmPreview> getAllViewedFilms(Context inputData) throws DAOException;
    public abstract void addFilmToViewedFilms(Activity act, TFilmPreview filmToAdd) throws DAOException;
    public abstract void removeFilmFromViewedFilms(Activity act, TFilmPreview filmToRemove) throws DAOException;
    public abstract boolean isFilmInDB(Activity act,String idGiven);

}
