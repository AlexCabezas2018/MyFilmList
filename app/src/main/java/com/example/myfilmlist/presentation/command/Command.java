package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.presentation.context.Context;

/**
 * This interface is useful to make that all the commands implement the method
 */

public interface Command {
    /**
     * Given a context, it returns data from the business layer.
     * @param inputData
     * @return Context
     */
    Context execute(Context inputData);
}
