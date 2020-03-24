package com.svintsov.players.misc;

/**
 * Exception while parsing command line arguments.
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
