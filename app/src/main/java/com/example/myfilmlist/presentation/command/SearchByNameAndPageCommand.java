package com.example.myfilmlist.presentation.command;

import android.util.Pair;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.film.filmappservice.ASFilm;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;

import java.util.List;

public class SearchByNameAndPageCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        Pair<List<TFilmPreview>, String> filmPreview = ASFilm.getInstance().searchByPage((Pair<String, Integer>) inputData.getData());
        return new Context(Events.SEARCH_BY_NAME_AND_PAGE, inputData.getActivity(), filmPreview);
    }
}
