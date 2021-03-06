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
            case SEARCH_BY_IMDB_ID:
                commandToReturn = new SearchByIMDBIDCommand();
                break;
            case SEARCH_BY_NAME_AND_PAGE:
                commandToReturn = new SearchByNameAndPageCommand();
                break;
            case ADD_REVIEW:
                commandToReturn = new SaveReviewCommand();
                break;
            case REMOVE_REVIEW:
                commandToReturn = new RemoveReviewCommand();
                break;
            case LOAD_REVIEW:
                commandToReturn = new LoadReviewCommand();
                break;
            case GET_ALL_VIEWED_FILMS:
                commandToReturn = new GetAllViewedFilmsCommand();
                break;
            case ADD_TO_FILMLIST:
                commandToReturn = new AddFilmToViewedFilmsCommand();
                break;
            case IS_FILM_IN_DB:
                commandToReturn = new IsFilmInDBCommand();
                break;
            case REMOVE_FROM_FILMLIST:
                commandToReturn = new RemoveFilmFromViewedFilmsCommand();
                break;
            case SHARE_REVIEW:
                commandToReturn = new ShareReviewCommand();
                break;
        }

        return commandToReturn;
    }
}
