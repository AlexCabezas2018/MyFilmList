package com.example.myfilmlist.business.review.reviewappservice;

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

    public abstract boolean saveReview(Context reviewToSave) throws ASException;
    public abstract TReview loadReview(Context reviewToLoad) throws ASException;
}
