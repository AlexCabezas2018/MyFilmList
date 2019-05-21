package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.business.review.reviewappservice.ASReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

public class RemoveReviewCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        ASReview.getInstance().removeReview(inputData.getActivity(), (String) inputData.getData());
        return new Context(inputData.getEvent(), inputData.getActivity(), null);
    }
}
