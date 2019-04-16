package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.business.film.TFilmFull;
import com.example.myfilmlist.business.film.filmappservice.ASFilm;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

public class SearchByIMDBIDCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        TFilmFull filmToReturn = ASFilm.getInstance().searchByIMDBId((String) inputData.getData());
        return new Context(inputData.getEvent(), inputData.getActivity(), filmToReturn);
    }
}
