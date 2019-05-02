package com.example.myfilmlist.business.review.reviewappservice;

import android.app.Activity;
import android.util.Pair;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daoreview.DAOReview;

public class ASReviewImp extends ASReview {

    /**
     * Saves a review and store it into the database. Returns true if the operation was successful
     * @param reviewToSave
     * @throws ASException
     */
    @Override
    public boolean saveReview(Pair<Activity, TReview> reviewToSave) throws ASException{
        try {
            if(reviewToSave.second.getContent().equals("")) throw new ASException("Content can't be empty!");
            if(reviewToSave.second.getImdbId().equals("")) throw new ASException("Imdb id can't be empty!");

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
    public TReview loadReview(Pair<Activity, Integer> reviewToLoad) throws ASException {
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
