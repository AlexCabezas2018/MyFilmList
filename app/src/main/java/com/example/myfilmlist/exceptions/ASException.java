package com.example.myfilmlist.exceptions;

import android.app.Activity;
import android.widget.Toast;

/**
 * This is used to throw exceptions from the business layer
 */

public class ASException extends Exception {
    private String reason;

    public ASException(String reason) {
        super(reason);
        this.reason = reason;
    }

    /**
     * This method shows the message in a toast form
     * @param view
     */
    public void showMessage(Activity view) {
        Toast.makeText(view, this.reason, (reason.length() > 30) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

}
