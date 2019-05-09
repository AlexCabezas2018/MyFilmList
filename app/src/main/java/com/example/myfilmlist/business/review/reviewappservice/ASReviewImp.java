package com.example.myfilmlist.business.review.reviewappservice;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.exceptions.DAOException;
import com.example.myfilmlist.integration.daofilm.DAOFilm;
import com.example.myfilmlist.integration.daoreview.DAOReview;
import com.example.myfilmlist.presentation.context.Context;

public class ASReviewImp extends ASReview {

    /**
     * Saves a review and store it into the database. Returns true if the operation was successful
     * @param reviewToSave
     * @throws ASException
     */
    @Override
    public boolean saveReview(Context reviewToSave) throws ASException{
        try {
            TReview toSave = (TReview) reviewToSave.getData();

            if(toSave.getContent().equals("")) throw new ASException("Content can't be empty!");
            if(toSave.getImdbId().equals("")) throw new ASException("Imdb id can't be empty!");

            TReview checkIfDontExists = loadReview(new Context(reviewToSave.getEvent(), reviewToSave.getActivity(), toSave.getImdbId()));
            if(checkIfDontExists != null) throw new ASException("There is a review for that film!");

            if (!DAOFilm.getInstance().isFilmInDB(reviewToSave.getActivity(), toSave.getImdbId())) throw new ASException("You nedd to view the film first!");

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
    public TReview loadReview(Context reviewToLoad) throws ASException {
        TReview reviewToReturn;
        try {
            reviewToReturn = DAOReview.getInstance().loadReview(reviewToLoad);
            if(reviewToReturn == null) return null;
            return reviewToReturn;
        }
        catch (DAOException exception) {
            throw new ASException(exception.getMessage());
        }

    }
}
