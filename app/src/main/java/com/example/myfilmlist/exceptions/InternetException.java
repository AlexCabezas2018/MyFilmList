package com.example.myfilmlist.exceptions;

import android.app.Activity;
import android.widget.Toast;

public class InternetException extends Exception {

    public InternetException() {
        super("There was a problem while trying to connect to internet");
    }
    public void showMessage(Activity view) {
        Toast.makeText(view, this.getMessage(), Toast.LENGTH_LONG).show();
    }
}
