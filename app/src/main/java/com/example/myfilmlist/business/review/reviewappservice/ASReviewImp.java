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
            DAOReview.getInstance().saveReview(reviewToSave);
            return true;
        }
        catch (DAOException exception) {
            throw new ASException(exception.getMessage());
        }
    }

    @Override
    public TReview loadReview(int imdbId) {
        return null;
    }
}
