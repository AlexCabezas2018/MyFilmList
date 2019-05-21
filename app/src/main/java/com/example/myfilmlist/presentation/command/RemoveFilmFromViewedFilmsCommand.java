package com.example.myfilmlist.presentation.command;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.business.film.filmappservice.ASFilm;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;

public class RemoveFilmFromViewedFilmsCommand implements Command {
    @Override
    public Context execute(Context inputData) throws ASException {
        Pair<TFilmPreview, RecyclerView.ViewHolder> data = (Pair<TFilmPreview, RecyclerView.ViewHolder>) inputData.getData();

        ASFilm.getInstance().removeViewedFilm(inputData.getActivity(), data.first);
        return new Context(inputData.getEvent(), inputData.getActivity(), data.second);
    }
}
