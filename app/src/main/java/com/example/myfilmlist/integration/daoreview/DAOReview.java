package com.example.myfilmlist.integration.daoreview;

import android.app.Activity;
import android.util.Pair;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.presentation.context.Context;

/**
 * This class is used to access to a local database and store and load data
 */

public abstract class DAOReview {
    private static DAOReview instance;

    public static DAOReview getInstance() {
        if(instance == null) instance = new DAOReviewImp();
        return instance;
    }

    public abstract void saveReview(Context reviewToSave) throws DAOException;
    public abstract TReview loadReview(Context toFind) throws DAOException;
    public abstract void removeReview(Activity activity, String reviewImdbId) throws DAOException;
}
