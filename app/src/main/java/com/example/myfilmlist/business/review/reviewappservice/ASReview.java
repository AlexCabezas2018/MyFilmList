package com.example.myfilmlist.business.review.reviewappservice;

import android.app.Activity;
import android.util.Pair;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;


/**
 * Class which has all the logic of the reviews
 */
public abstract class ASReview {
    private static ASReview instance;

    public static ASReview getInstance() {
        if(instance == null) instance = new ASReviewImp();
        return instance;
    }

    public abstract boolean saveReview(Pair<Activity, TReview> reviewToSave) throws ASException;
    public abstract TReview loadReview(int imdbId);
}
