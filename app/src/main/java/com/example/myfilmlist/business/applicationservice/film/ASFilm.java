package com.example.myfilmlist.business.applicationservice.film;

import com.example.myfilmlist.business.applicationservice.TFilmFull;
import com.example.myfilmlist.business.applicationservice.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;

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

    public abstract List<TFilmPreview> searchByName(String filmName) throws ASException;

}
