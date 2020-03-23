package com.svintsov.players;

import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Objects;

/**
 * Application.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class Application {

    public static final Integer MAX_MESSAGES = 10;
    public static final Integer PORT_NUMBER = 8080;
    private static final String MODE_OPTION = "mode";

    public static void main(String[] args) {

        Options options = new Options();
        options.addOption(new Option("m", MODE_OPTION, true, "Mode: either sender or receiver"));

        CommandLineParser parser = new DefaultParser();
        Try.of(() -> parser.parse(options, args)).toEither()
                .mapLeft(throwable -> new CommandLineException("arguments parsing error", throwable))
                .map(commandLine -> commandLine.getOptionValue(MODE_OPTION))
                .flatMap(modeValue -> Objects.isNull(modeValue) ? Either.left(new CommandLineException("mode must be specified")) : Either.right(modeValue))
                .flatMap(modeValue -> io.vavr.control.Option.ofOptional(ApplicationMode.findByValue(modeValue)).toEither(new CommandLineException("invalid mode value specified")))
                .peekLeft(commandLineException -> log.error("Error while parsing command line arguments:", commandLineException))
                .peekLeft(commandLineException -> new HelpFormatter().printHelp("ant", options))
                .peek(Application::startInMode);
    }

    private static void startInMode(ApplicationMode mode) {
        Player.createWithMode(mode).start();
    }

}
