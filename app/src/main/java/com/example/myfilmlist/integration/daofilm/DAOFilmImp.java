package com.example.myfilmlist.integration.daofilm;

import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DAOFilmImp extends DAOFilm{
    private static String OMDB_URL_FOR_NAME = "http://www.omdbapi.com/?s=¿&page=1&apikey=60b46c14"; //This is used to search by name, we find a film where "¿" = name
    private static String OMDB_URL_NAME_AND_PAGE = "http://www.omdbapi.com/?s=¿&page=*&apikey=60b46c14"; //This is used to get more films (if there is a lot o results)
    private static String OMBD_URL_IMDB_ID = "http://www.omdbapi.com/?i=¿&apikey=60b46c14"; //This is used to search a film with the imdb id

    private JSONObject databaseResponse; //To store the result from the API
    private String exceptionResponse; //Just in case the thread throws an exception (the run() method can not throw any exception)


    /**
     *
     * Given a name, returns a list with the basic information of the films which titles contains the name.
     * @param filmName
     * @return Collection
     * @throws DAOException
     */
    @Override
    public List<TFilmPreview> getFilmsByName(final String filmName) throws DAOException {
        exceptionResponse = "";
        databaseResponse = null; //We clear these two variables every time we start an operation.
        List<TFilmPreview> filmsToReturn = new ArrayList<>();
        final CountDownLatch counter = new CountDownLatch(1); //This is used to wait to the thread to finish. Its a counter which starts from 1.

        try {
            Thread resourceThread = new Thread(new Runnable() { //We create a thread to run our process (In android, its impossible to execute logic in the main Thread)
                @Override
                public void run() {
                    try {
                        String urlToFind = OMDB_URL_FOR_NAME.replace("¿", filmName); //We substitute the letter "¿" with the name of the film
                        databaseResponse = JSONUtils.readJsonFromUrl(urlToFind); //We obtain the response from the api
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
            if (response.equals("False")) return null; //That means there is no film with that name.

            filmsToReturn = getFilmsFromJSON(databaseResponse);

        }
        catch (DAOException | InterruptedException | JSONException exception) {
            throw new DAOException("There was a problem while trying to get the film/s ("
                                    + exception.getMessage() + ").");
        }
        return filmsToReturn;
    }

    @Override
    public TFilmFull getFilmByIMDBId(String id) throws DAOException {
        //TODO implement the method.
        return null;
    }

    /*  UTILS  */

    /**
     * Given a JSON which contains films, it returns a list of films with basic information.
     * @param jsonObject
     * @return Collection
     * @throws JSONException
     */
    private static List<TFilmPreview> getFilmsFromJSON(JSONObject jsonObject) throws JSONException {
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

        return filmsToReturn;
    }
}
