package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.presentation.context.Context;

/**
 * This class is used to transform events into commands
 */

public class CommandDispatcher {

    /**
     * Given a context, it returns a command based on the event
     * @param context
     * @return Command
     */
    public static Command dispatchCommand(Context context) {
        Command commandToReturn = null;
        switch (context.getEvent()) {
            case SEARCH_BY_NAME:
                commandToReturn = new SearchByNameCommand();
                break;
            case ADD_REVIEW:
                break;
            case REMOVE_REVIEW:
                break;
            case ADD_TO_FILMLIST:
                break;
            case REMOVE_FROM_FILMLIST:
                break;
            case REFRESH_FILM_LIST:
                break;
        }

        return commandToReturn;
    }
}
