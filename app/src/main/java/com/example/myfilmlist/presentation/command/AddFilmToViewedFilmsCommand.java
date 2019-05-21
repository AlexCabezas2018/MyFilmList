package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.film.filmappservice.ASFilm;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

public class AddFilmToViewedFilmsCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        ASFilm.getInstance().addViewedFilm(inputData.getActivity(), (TFilmPreview) inputData.getData());
        return new Context(inputData.getEvent(), inputData.getActivity(), null);
    }
}
