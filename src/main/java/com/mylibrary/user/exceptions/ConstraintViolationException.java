package com.mylibrary.user.exceptions;

/**
 * This exception should be thrown whenever integrity violation is happening.
 */
@SuppressWarnings("unused")
public class ConstraintViolationException extends RuntimeException {

    /**
     * Instantiates a new Constraint violation exception.
     *
     * @param message the message
     */
    public ConstraintViolationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Constraint violation exception.
     *
     * @param message the message
     * @param cause   the cause {@link Throwable}
     */
    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
