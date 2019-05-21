package com.example.myfilmlist.business.review.reviewappservice;

import android.app.Activity;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;


/**
 * Class which has all the logic of the reviews
 */
public abstract class ASReview {
    private static ASReview instance;

    public static ASReview getInstance() {
        if(instance == null) instance = new ASReviewImp();
        return instance;
    }

    public abstract boolean saveReview(Activity activity, TReview reviewToSave) throws ASException;
    public abstract TReview loadReview(Activity activity, String reviewId) throws ASException;
    public abstract void removeReview(Activity activity, String reviewImdbId) throws ASException;
    public abstract String shareReview(String title, String review, Activity activity) throws ASException;
}
