package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.business.film.filmappservice.ASFilm;
import com.example.myfilmlist.presentation.context.Context;

public class IsFilmInDBCommand implements Command {

    @Override
    public Context execute(Context inputData){
        boolean toReturn = ASFilm.getInstance().isFilmInDB(inputData.getActivity(), (String) inputData.getData());
        return new Context(inputData.getEvent(), inputData.getActivity(), toReturn);
    }
}