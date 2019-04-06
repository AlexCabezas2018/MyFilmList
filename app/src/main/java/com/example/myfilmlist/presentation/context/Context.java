package com.example.myfilmlist.presentation.context;

import com.example.myfilmlist.presentation.events.Events;
import com.example.myfilmlist.presentation.views.UpdatingView;

/**
 * This class is used to transport data between layers,
 * abstracting the type of the data. It contains the activity which creates the context.
 */

public class Context {
    private Events event;
    private Object data;
    private UpdatingView callerActivity;

    public Context(Events event, UpdatingView callerActivity, Object data) {
        this.event = event;
        this.callerActivity = callerActivity;
        this.data  = data;
    }

    public Events getEvent() {
        return event;
    }

    public UpdatingView getActivity() {
        return this.callerActivity;
    }

    public Object getData() {
        return this.data;
    }
}
