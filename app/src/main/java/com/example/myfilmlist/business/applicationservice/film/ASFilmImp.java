package com.example.myfilmlist.business.applicationservice.film;

import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daofilm.DAOFilm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class ASFilmImp extends ASFilm {

    /**
     * Given a name, it returns the film or films that contains the title
     * @param filmName
     * @return
     * @throws ASException
     */
    @Override
    public List<TFilm> searchByName(String filmName) throws ASException {
        List<TFilm> filmsToReturn;
        try{
            String resultFilmName = URLEncoder.encode(filmName, "UTF-8"); //We encode our data in UTF-8 (because the api needs the encoded filmName)
            filmsToReturn = DAOFilm.getInstance().getFilmsByName(resultFilmName); //We call the data layer and obtain the films
            if(filmsToReturn == null) throw new ASException("The film doesn't exist"); //That means the film does not exist
        }
        catch(DAOException | ASException | UnsupportedEncodingException exception) {
            throw new ASException(exception.getMessage());
        }
        return filmsToReturn;
    }
}
