package com.svintsov.players.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Operating mode of application.
 * {@link #SENDER} - start player sending messages
 * {@link #RECEIVER} - start player receiving messages
 * {@link #STANDALONE} - start both player in one process
 *
 * @author Ilya_Svintsov
 */
@Getter
@RequiredArgsConstructor
public enum ApplicationMode {

    SENDER("sender"),
    RECEIVER("receiver"),
    STANDALONE("standalone");

    private final String value;

    public static Optional<ApplicationMode> findByValue(String value) {
        return Stream.of(values())
                .filter(mode -> Objects.equals(mode.getValue(), value))
                .findFirst();
    }
}
