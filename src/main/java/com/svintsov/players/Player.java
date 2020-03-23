package com.svintsov.players;

/**
 * Player.
 *
 * @author Ilya_Svintsov
 */
public abstract class Player {

    public abstract ApplicationMode getMode();

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

