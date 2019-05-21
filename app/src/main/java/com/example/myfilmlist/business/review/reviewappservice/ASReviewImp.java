package com.example.myfilmlist.business.review.reviewappservice;

import android.app.Activity;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daofilm.DAOFilm;
import com.example.myfilmlist.integration.daoreview.DAOReview;

public class ASReviewImp extends ASReview {

    /**
     * Saves a review and store it into the database. Returns true if the operation was successful
     * @param reviewToSave
     * @throws ASException
     */
    @Override
    public boolean saveReview(Activity activity, TReview reviewToSave) throws ASException {
        try {

            if(reviewToSave.getContent().equals("")) throw new ASException("Content can't be empty!");
            if(reviewToSave.getImdbId().equals("")) throw new ASException("Imdb id can't be empty!");

            TReview checkIfDontExists = DAOReview.getInstance().loadReview(activity, reviewToSave.getImdbId());
            if(checkIfDontExists != null) throw new ASException("There is a review for that film!");

            if (!DAOFilm.getInstance().isFilmInDB(activity, reviewToSave.getImdbId())) throw new ASException("You need to view the film first!");

            DAOReview.getInstance().saveReview(activity, reviewToSave);
            return true;
        }
        catch (DAOException exception) {
            throw new ASException(exception.getMessage());
        }
    }


    /**
     * Returns a review, given a correct imdb id.
     * @param activity (necessary to instantiate the db) and a String.
     * @return null if the review doesn't exist
     * @return TReview if the review exists
     */
    @Override
    public TReview loadReview(Activity activity, String reviewId) throws ASException {
        TReview reviewToReturn;
        try {
            reviewToReturn = DAOReview.getInstance().loadReview(activity, reviewId);
            if(reviewToReturn == null) return null;
            return reviewToReturn;
        }
        catch (DAOException exception) {
            throw new ASException(exception.getMessage());
        }

    }

    @Override
    public void removeReview(Activity activity, String reviewImdbId) throws ASException {
        try{
            DAOReview.getInstance().removeReview(activity, reviewImdbId);
        }
        catch (DAOException exception) {
            throw new ASException(exception.getMessage());
        }
    }

    /**
     * Prepares a String with the information to share
     * @param title
     * @param review
     * @return String
     */
    @Override
    public String shareReview(String title, String review, Activity activity) throws ASException {
        String resultOutput = "";
        if(title == null || review == null) throw new ASException("Error while preparing the review (title or review are not defined!");
        TFilmPreview film = DAOFilm.getInstance().searchFilmInViewedByTitle(title, activity);
        if(film == null) throw new ASException("There was a problem while preparing the review (the film is not viewed!");

        resultOutput += "Hey! Look what I just posted about " + title +"!\n\n";
        resultOutput += "\uD83C\uDFAC Review \uD83C\uDFAC\n\n";
        resultOutput += review + "\n\n";
        resultOutput += "You can see more about this film with the following link: " + "https://www.imdb.com/title/" + film.getImdbID() + "/\n\n";
        return resultOutput;
    }


}
