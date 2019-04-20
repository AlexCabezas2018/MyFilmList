package com.example.myfilmlist.business.film.filmappservice;

import android.util.Pair;

import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daofilm.DAOFilm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class ASFilmImp extends ASFilm {

    /**
     * Given an IMDB id, it returns the Film with all the information.
     * @param id
     * @return TFilmFull
     * @throws ASException
     */
    @Override
    public TFilmFull searchByIMDBId(String id) throws ASException {
        try{
            TFilmFull filmToReturn = DAOFilm.getInstance().getFilmByIMDBId(id); //Get the film
            if(filmToReturn == null) throw new ASException("The film doesn't exist"); //Then the film doesn't exist
            else return filmToReturn;

        } catch (DAOException e) {
            throw new ASException(e.getMessage());
        }
    }


    /**
     * Given a string and a page number, it returns the films in the indicated page and the number of available pages.
     * @param nameAndPage
     * @return
     * @throws ASException
     */
    @Override
    public Pair<List<TFilmPreview>, String> searchByPage(Pair<String, Integer> nameAndPage) throws ASException {
        try{
            String urlToEncode = URLEncoder.encode(nameAndPage.first, "UTF-8"); //We encode our data in UTF-8 (because the api needs the encoded filmName)
            Pair<String, Integer> pairToSearch = new Pair<>(urlToEncode, nameAndPage.second); //Create a new Pair with the encoded title
            Pair<List<TFilmPreview>, String> filmsToReturn = DAOFilm.getInstance().getFilmsFromNextPage(pairToSearch);
            if(filmsToReturn == null && nameAndPage.second == 1) throw new ASException("The film doesn't exist");
            if(filmsToReturn == null) throw new ASException("No more results for '" + nameAndPage.first + "'."); //Means that we have reached the total amount of pages.
            else return filmsToReturn;

        } catch (DAOException | UnsupportedEncodingException exception) {
            throw new ASException(exception.getMessage());
        }
    }
}
