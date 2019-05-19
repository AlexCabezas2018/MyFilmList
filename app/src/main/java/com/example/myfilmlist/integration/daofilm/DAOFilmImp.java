package com.example.myfilmlist.integration.daofilm;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.util.Pair;

import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.utils.FilmUtils;
import com.example.myfilmlist.integration.utils.JSONUtils;
import com.example.myfilmlist.presentation.context.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DAOFilmImp extends DAOFilm {

    private static String OMDB_URL_NAME_AND_PAGE = "http://www.omdbapi.com/?s=¿&page=@&apikey=60b46c14"; //This is used to get more films (if there is a lot o results)
    private static String OMDB_URL_IMDB_ID = "http://www.omdbapi.com/?i=¿&apikey=60b46c14"; //This is used to search a film with the imdb id

    private JSONObject databaseResponse; //To store the result from the API
    private String exceptionResponse; //Just in case the thread throws an exception (the run() method can not throw any exception)


    @Override
    public TFilmFull getFilmByIMDBId(final String id) throws DAOException {
        exceptionResponse = "";
        databaseResponse = null; //We clear these two variables every time we start an operation.
        TFilmFull filmToReturn = null;
        final CountDownLatch counter = new CountDownLatch(1); //This is used to wait to the thread to finish. Its a counter which starts from 1.

        try {
            Thread resourceThread = new Thread(new Runnable() { //We create a thread to run our process (In android, its impossible to execute logic in the main Thread)
                @Override
                public void run() {
                    try {
                        String urlToFind = OMDB_URL_IMDB_ID.replace("¿", id); //We substitute the letter "¿" with the imdb id of the film
                        databaseResponse = JSONUtils.readJsonFromUrl(urlToFind); //We obtain the response from the api
                    } catch (IOException | JSONException exception) {
                        exceptionResponse = exception.getMessage(); //If we get an error, the variable will be != "" and we store the message of the exception
                    }
                    counter.countDown(); //We count down the counter we defined before. With this, the thread is saying it finished
                }
            });
            resourceThread.start(); //Get the data, executing the thread
            counter.await(); //Wait the thread to end

            if (!exceptionResponse.equals(""))
                throw new DAOException(exceptionResponse); //There was an exception

            String response = databaseResponse.getString("Response"); //We obtain the attribute "Response" from the JSON
            if (response.equals("False")) {
                String error = databaseResponse.getString("Error");
                if(error.equals("Movie not found!")) return null;
                else throw new DAOException(error);
            }

            filmToReturn = getFullFilmFromJSON(databaseResponse);
        }
        catch (DAOException | InterruptedException | JSONException exception) {
            throw new DAOException("There was a problem while trying to get the film/s ("
                    + exception.getMessage() + ").");
        }

        return filmToReturn;
    }

    /**
     * Given a name and a page number, it returns at least 10 films with basic information,
     * according to the page.
     * @param inputInformation
     * @return
     */
    public Pair<List<TFilmPreview>, String> getFilmsFromNextPage(final Pair<String, Integer> inputInformation) throws DAOException{
        exceptionResponse = "";
        databaseResponse = null; //We clear these two variables every time we start an operation.

        String urlToFind = OMDB_URL_NAME_AND_PAGE.replace("¿", inputInformation.first); //We substitute the letter "¿" with the name of the film
        final String urlToGet = urlToFind.replace("@", inputInformation.second.toString()); //We substitute the letter "@" with the page we request.

        Pair<List<TFilmPreview>, String> filmsToReturn; //It will contain the results and the total number of pages.
        final CountDownLatch counter = new CountDownLatch(1); //This is used to wait to the thread to finish. Its a counter which starts from 1.

        try {
            Thread resourceThread = new Thread(new Runnable() { //We create a thread to run our process (In android, its impossible to execute logic in the main Thread)
                @Override
                public void run() {
                    try {
                        databaseResponse = JSONUtils.readJsonFromUrl(urlToGet); //We obtain the response from the api
                    } catch (IOException | JSONException exception) {
                        exceptionResponse = exception.getMessage(); //If we get an error, the variable will be != "" and we store the message of the exception
                    }
                    counter.countDown(); //We count down the counter we defined before. With this, the thread is saying it finished
                }
            });
            resourceThread.start(); //We start our thread
            counter.await(); //We wait to the counter to get 0

            if (!exceptionResponse.equals(""))
                throw new DAOException(exceptionResponse); //There was an exception

            String response = databaseResponse.getString("Response"); //We obtain the attribute "Response" from the JSON
            if (response.equals("False")) {
                String error = databaseResponse.getString("Error");
                if(error.equals("Movie not found!")) return null;
                else throw new DAOException(error);
            } //That means there is no film with that name.

            filmsToReturn = getPreviewFilmsFromJSON(databaseResponse);

        }
        catch (InterruptedException | JSONException exception) {
            throw new DAOException("There was a problem while trying to get the film/s ("
                    + exception.getMessage() + ").");
        }
        return filmsToReturn;
    }

    /**
     * Return all the viewed films of the user
     * @param inputData
     * @return
     * @throws DAOException
     */
    @Override
    public List<TFilmPreview> getAllViewedFilms(Context inputData) throws DAOException {
        List<TFilmPreview> films;
        try {
            SQLiteHandlerViewedFilms database = new SQLiteHandlerViewedFilms(inputData.getActivity(), null, null, 1);
            films = database.getAllViewedFilms();
            return films;
        }
        catch (DAOException exception) {
            throw new DAOException("There was a problem while trying to get the films/s (" + exception.getMessage() + ").");
        }
    }

    @Override
    public void addFilmToViewedFilms(Activity act, TFilmPreview filmToAdd) throws DAOException {
        try{
            SQLiteHandlerViewedFilms database = new SQLiteHandlerViewedFilms(act, null, null, 1);
            database.insertFilm(filmToAdd);
        }
        catch (SQLiteException exception) {
            throw new DAOException("Problem while adding a film to the viewed ones ( " + exception.getMessage() + " ).");
        }
    }

    @Override
    public boolean isFilmInDB(Activity act,String idGiven) {
        SQLiteHandlerViewedFilms database = new SQLiteHandlerViewedFilms(act, null, null, 1);
        return database.isFilmInDB(idGiven);
    }

    @Override
    public void removeFilmFromViewedFilms(Activity act, TFilmPreview filmToRemove) throws DAOException{
        try{
            SQLiteHandlerViewedFilms database = new SQLiteHandlerViewedFilms(act, null, null, 1);
            database.daleteFilm(filmToRemove);
        }
        catch (SQLiteException exception) {
            throw new DAOException("Problem while removing a film from the viewed ones ( " + exception.getMessage() + " ).");
        }
    }


    /*  UTILS  */

    /**
     * Given a JSON which contains films, it returns a list of films with basic information.
     * @param jsonObject
     * @return Collection
     * @throws JSONException
     */
    private static Pair<List<TFilmPreview>, String> getPreviewFilmsFromJSON(JSONObject jsonObject) throws JSONException {
        List<TFilmPreview> filmsToReturn = new ArrayList<>(); //This is the list we are gonna return

        JSONArray films = jsonObject.getJSONArray("Search"); //We go into the "search" label, which contains the films
        for (int i = 0; i < films.length(); i++) { //We store each film of the array (max = 10)
            TFilmPreview filmPreview = new TFilmPreview();
            JSONObject film = films.getJSONObject(i);

            /*  Fill the object with the information */
            filmPreview.setTitle(film.getString("Title"));
            filmPreview.setYear(film.getString("Year"));
            filmPreview.setImdbID(film.getString("imdbID"));
            filmPreview.setType(film.getString("Type"));
            filmPreview.setImageURL(film.getString("Poster"));

            filmsToReturn.add(filmPreview);
        }

        /* Getting the total results, and the number of pages */
        Integer numberOfResults = jsonObject.getInt("totalResults");
        String numberOfPages = FilmUtils.numberOfPages(numberOfResults);

        return new Pair<>(filmsToReturn, numberOfPages);
    }


    /**
     * Extracts the film from the JSON and return a film with full information
     * @param jsonObject
     * @return TFilmFull
     * @throws JSONException
     */
    private static TFilmFull getFullFilmFromJSON(JSONObject jsonObject) throws JSONException{
        TFilmFull filmToReturn = new TFilmFull();

        /* Set the information we get */
        filmToReturn.setTitle(jsonObject.getString("Title"));
        filmToReturn.setImageURL(jsonObject.getString("Poster"));
        filmToReturn.setGenre(jsonObject.getString("Genre"));
        filmToReturn.setYear(jsonObject.getString("Year"));
        filmToReturn.setDuration(FilmUtils.transformTime(jsonObject.getString("Runtime")));
        filmToReturn.setDirectors(jsonObject.getString("Director"));
        filmToReturn.setActors(jsonObject.getString("Actors"));
        filmToReturn.setPlot(jsonObject.getString("Plot"));
        filmToReturn.setRate(jsonObject.getString("imdbRating"));
        filmToReturn.setType(jsonObject.getString("Type"));
        filmToReturn.setImbdid(jsonObject.getString("imdbID"));

        return filmToReturn;
    }
}
