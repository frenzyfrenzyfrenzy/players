package com.svintsov.players;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * ApplicationMode.
 *
 * @author Ilya_Svintsov
 */
@Getter
@RequiredArgsConstructor
public enum ApplicationMode {

    SENDER("sender"),
    RECEIVER("receiver");

    private final String value;

    public static Optional<ApplicationMode> findByValue(String value) {
        return Stream.of(values())
                .filter(mode -> Objects.equals(mode.getValue(), value))
                .findFirst();
    }
}
