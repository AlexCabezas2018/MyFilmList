package com.example.myfilmlist.business.review.reviewappservice;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daoreview.DAOReview;
import com.example.myfilmlist.presentation.context.Context;

public class ASReviewImp extends ASReview {

    /**
     * Saves a review and store it into the database. Returns true if the operation was successful
     * @param reviewToSave
     * @throws ASException
     */
    @Override
    public boolean saveReview(Context reviewToSave) throws ASException{
        try {
            TReview toSave = (TReview) reviewToSave.getData();
            //TODO comprobar que la review ya existe en la base de datos.

            if(toSave.getContent().equals("")) throw new ASException("Content can't be empty!");
            if(toSave.getImdbId().equals("")) throw new ASException("Imdb id can't be empty!");

            DAOReview.getInstance().saveReview(reviewToSave);
            return true;
        }
        catch (DAOException exception) {
            throw new ASException(exception.getMessage());
        }
    }


    /**
     * Returns a review, given a correct imdb id.
     * @param reviewToLoad: a combination of an activity (necessary to instantiate the db) and an integer.
     * @return null if the review doesn't exist
     * @return TReview if the review exists
     */
    @Override
    public TReview loadReview(Context reviewToLoad) throws ASException {
        TReview reviewToReturn;
        try {
            reviewToReturn = DAOReview.getInstance().loadReview(reviewToLoad);
            if(reviewToReturn == null) throw new ASException("The review doesn't exists");
            return reviewToReturn;
        }
        catch (DAOException exception) {
            throw new ASException(exception.getMessage());
        }

    }
}
