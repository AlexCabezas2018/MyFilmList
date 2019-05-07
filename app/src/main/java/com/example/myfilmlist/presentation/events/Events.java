package com.example.myfilmlist.presentation.events;

/**
 * This enums are used to tell the system the action that the app wants to do.
 */

public enum Events {
    SEARCH_BY_IMDB_ID,
    SEARCH_BY_NAME_AND_PAGE,
    ADD_REVIEW,
    REMOVE_REVIEW,
    LOAD_REVIEW,
    GET_ALL_VIEWED_FILMS,
    ADD_TO_FILMLIST,
    REMOVE_FROM_FILMLIST,
    REFRESH_FILM_LIST,
}
