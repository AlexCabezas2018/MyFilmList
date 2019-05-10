package com.example.myfilmlist.presentation.utils;

import android.view.View;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.context.Context;
import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.presenter.Presenter;
import com.example.myfilmlist.presentation.views.UpdatingView;

public class ParameterOnClickListener implements View.OnClickListener{

    private TFilmPreview parameter;
    private UpdatingView view;

    public ParameterOnClickListener(TFilmPreview parameter, UpdatingView view){
        this.parameter = parameter;
        this.view = view;
    }
    @Override
    public void onClick(View v) {
        try {
            Presenter.getInstance().action(new Context(Events.ADD_TO_FILMLIST, view, parameter));
        } catch (ASException ex) {
            ex.showMessage(view);
        }
    }
}
