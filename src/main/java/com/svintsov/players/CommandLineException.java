package com.svintsov.players;

/**
 * CommandLineException.
 *
 * @author Ilya_Svintsov
 */
public class CommandLineException extends RuntimeException {

    public CommandLineException(String message) {
        super(message);
    }

    public CommandLineException(String message, Throwable cause) {
        super(message, cause);
    }
}
