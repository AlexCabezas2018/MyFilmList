package com.example.myfilmlist.presentation.command;

import android.util.Pair;
import com.example.myfilmlist.business.review.reviewappservice.ASReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

public class ShareReviewCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        Pair<String, String> data = (Pair<String, String>) inputData.getData();
        String toReturn = ASReview.getInstance().shareReview(data.first, data.second, inputData.getActivity());
        return new Context(inputData.getEvent(), inputData.getActivity(), toReturn);

    }
}
