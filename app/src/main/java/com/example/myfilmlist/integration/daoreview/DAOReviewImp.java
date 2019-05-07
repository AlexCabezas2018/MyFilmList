package com.example.myfilmlist.integration.daoreview;

import android.database.sqlite.SQLiteException;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.presentation.context.Context;

public class DAOReviewImp extends DAOReview {

    /**
     * Saves a review locally
     * @param reviewToSave
     * @throws DAOException
     */
    @Override
    public void saveReview(Context reviewToSave) throws DAOException {
        try {
            SQLiteHandlerReviews database = new SQLiteHandlerReviews(reviewToSave.getActivity(), null, null, 1); //Creating an instance to the database
            database.addReview((TReview) reviewToSave.getData()); //Invoke the add method
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
    public TReview loadReview(Context toFind) throws DAOException {
        TReview toReturn;
        try {
            SQLiteHandlerReviews database = new SQLiteHandlerReviews(toFind.getActivity(), null, null, 1); //Creating an instance to the database
            toReturn = database.getReviewFromIMDBId((Integer) toFind.getData()); //Invoke the get method.
            return toReturn;
        }
        catch (DAOException | SQLiteException exception) {
            throw new DAOException("There was an exception while saving the review (" + exception.getMessage() + ")");
        }
    }
}
