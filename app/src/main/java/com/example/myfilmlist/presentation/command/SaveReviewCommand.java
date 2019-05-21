package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.business.review.reviewappservice.ASReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

public class SaveReviewCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        boolean toReturn = ASReview.getInstance().saveReview(inputData.getActivity(), (TReview) inputData.getData());
        return new Context(inputData.getEvent(), inputData.getActivity(), toReturn);
    }
}
