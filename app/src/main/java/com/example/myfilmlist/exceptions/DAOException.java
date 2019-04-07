package com.example.myfilmlist.exceptions;

/**
 * This is used to throw exceptions from the integration layer
 */

public class DAOException extends Exception {
    private String reason;

    public DAOException() {
        super();
    }

    public DAOException(String reason) {
        super(reason);
        this.reason = reason;
    }
}
