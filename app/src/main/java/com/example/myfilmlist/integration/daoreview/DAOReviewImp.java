package com.example.myfilmlist.integration.daoreview;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.util.Pair;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daoreview.sqlitehandler.SQLiteHandlerReviews;

public class DAOReviewImp extends DAOReview {

    /**
     * Saves a review locally
     * @param review
     * @throws DAOException
     */
    @Override
    public void saveReview(final Pair<Activity, TReview> review) throws DAOException {
        try {
            SQLiteHandlerReviews database = new SQLiteHandlerReviews(review.first, null, null, 1); //Creating an instance to the database
            database.addReview(review.second); //Invoke the add method
        } catch (SQLiteException exception) {
            throw new DAOException("There was an exception while saving the review (" + exception.getMessage() + ")");
        }
    }


    /**
     * Loads a review given the imdbid. Returns null if the product doesn't exists.
     * @param toFind
     * @return TReview
     */
    @Override
    public TReview loadReview(Pair<Activity, Integer> toFind) throws DAOException {
        TReview toReturn;
        try {
            SQLiteHandlerReviews database = new SQLiteHandlerReviews(toFind.first, null, null, 1); //Creating an instance to the database
            toReturn = database.getReviewFromIMDBId(toFind.second); //Invoke the get method.
            return toReturn;
        }
        catch (DAOException | SQLiteException exception) {
            throw new DAOException("There was an exception while saving the review (" + exception.getMessage() + ")");
        }
    }
}
