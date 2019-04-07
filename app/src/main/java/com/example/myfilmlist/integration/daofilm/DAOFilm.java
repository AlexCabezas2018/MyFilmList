package com.example.myfilmlist.integration.daofilm;

import com.example.myfilmlist.business.applicationservice.film.TFilm;
import com.example.myfilmlist.exceptions.DAOException;

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

    public abstract List<TFilm> getFilmsByName(String filmName) throws DAOException;

}
