package com.example.myfilmlist.presentation.presenter;

/**
 * This class communicates the presentation layer with the business layer through the commands.
 * In this case, we must use a singleton to make sure we have only one instance of the class in the app.
 */

import com.example.myfilmlist.presentation.context.Context;

public abstract class Presenter {

    /* SINGLETON */
    private static Presenter instance = null;

    public static Presenter getInstance() {
        if(instance == null) {
            instance = new PresenterImp();
        }
        return instance;
    }

    public abstract void action(Context context);

}
