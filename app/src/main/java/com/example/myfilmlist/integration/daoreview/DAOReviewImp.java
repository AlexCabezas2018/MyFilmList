package com.example.myfilmlist.integration.daoreview;

import android.app.Activity;
import android.database.sqlite.SQLiteException;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.DAOException;

public class DAOReviewImp extends DAOReview {

    /**
     * Saves a review locally
     * @param activity, reviewToSave
     * @throws DAOException
     */
    @Override
    public void saveReview(Activity activity, TReview reviewToSave) throws DAOException {
        try {
            SQLiteHandlerReviews database = new SQLiteHandlerReviews(activity, null, null, 1); //Creating an instance to the database
            database.addReview(reviewToSave); //Invoke the add method
        } catch (SQLiteException exception) {
            throw new DAOException("There was an exception while saving the review (" + exception.getMessage() + ")");
        }
    }


    /**
     * Loads a review given the imdbid. Returns null if the product doesn't exists.
     * @param activity, imdbid
     * @return TReview
     */
    @Override
    public TReview loadReview(Activity activity, String imdbId) throws DAOException {
        TReview toReturn;
        try {
            SQLiteHandlerReviews database = new SQLiteHandlerReviews(activity, null, null, 1); //Creating an instance to the database
            toReturn = database.getReviewFromIMDBId(imdbId); //Invoke the get method.
            return toReturn;
        }
        catch (DAOException | SQLiteException exception) {
            throw new DAOException("There was an exception while loading the review (" + exception.getMessage() + ")");
        }
    }

    /**
     * Delete a review
     * @param activity
     * @param reviewImdbId
     * @throws DAOException
     */
    @Override
    public void removeReview(Activity activity, String reviewImdbId) throws DAOException {
        try{
            SQLiteHandlerReviews database = new SQLiteHandlerReviews(activity, null, null, 1);
            database.deleteReview(reviewImdbId);
        }
        catch (DAOException exception) {
            throw new DAOException("Problem while removing a review ( " + exception.getMessage() + " ).");
        }
    }
}
