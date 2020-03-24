package com.svintsov.players.player;

import com.svintsov.players.misc.ApplicationMode;

/**
 * General {@link Player} abstraction, that can send and receive messages.
 * Can be either {@link ApplicationMode#SENDER} or {@link ApplicationMode#RECEIVER}.
 * Use factory method {@link #createWithMode(ApplicationMode)} to create new instances.
 * Use {@link #start()} to start accepting/sending messages.
 *
 * @author Ilya_Svintsov
 */
public abstract class Player {

    public abstract void start();

    public static Player createWithMode(ApplicationMode mode) {
        switch (mode) {
            case SENDER:
                return new Client();
            case RECEIVER:
                return new Server();
            default:
                throw new IllegalArgumentException(String.format("Mode %s is not supported", mode));
        }
    }

}

