package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.film.filmappservice.ASFilm;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

import java.util.List;

public class GetAllViewedFilmsCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        List<TFilmPreview> filmsToReturn = ASFilm.getInstance().getAllViewedFilms(inputData.getActivity());
        return new Context(inputData.getEvent(), inputData.getActivity(), filmsToReturn);
    }
}
