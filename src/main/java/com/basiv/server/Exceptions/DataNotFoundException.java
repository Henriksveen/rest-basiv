package com.basiv.server.Exceptions;

/**
 * @author Henriksveen
 */
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }
}
