package com.example.myfilmlist.integration.daofilm;

import com.example.myfilmlist.business.applicationservice.TFilmFull;
import com.example.myfilmlist.business.applicationservice.TFilmPreview;
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

    public abstract List<TFilmPreview> getFilmsByName(String filmName) throws DAOException;

}
