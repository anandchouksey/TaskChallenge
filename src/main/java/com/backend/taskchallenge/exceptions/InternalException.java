package com.backend.taskchallenge.exceptions;

public class InternalException extends RuntimeException{
    public InternalException(final Exception exception) {
        super(exception);
    }

    public InternalException(final String message) {
        super(message);
    }
}
