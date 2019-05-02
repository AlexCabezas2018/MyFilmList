package com.example.myfilmlist.presentation.command;

import android.app.Activity;
import android.util.Pair;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.business.review.reviewappservice.ASReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

public class LoadReviewCommand implements Command {

    @Override
    public Context execute(Context inputData) throws ASException {
        Pair<Activity, Integer> data = (Pair<Activity, Integer>) inputData.getData();
        TReview review = ASReview.getInstance().loadReview(data);
        return new Context(inputData.getEvent(), inputData.getActivity(), review);
    }
}
