package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.business.applicationservice.TFilmPreview;
import com.example.myfilmlist.business.applicationservice.film.ASFilm;
import com.example.myfilmlist.business.applicationservice.TFilmFull;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

import java.util.List;

public class SearchByNameCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        List<TFilmPreview> result = ASFilm.getInstance().searchByName((String)inputData.getData()); //We call to the business layer and obtain the data
        return new Context(inputData.getEvent(), inputData.getActivity(), result);
    }
}
