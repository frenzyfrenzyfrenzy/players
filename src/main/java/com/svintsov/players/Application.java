package com.svintsov.players;

import com.svintsov.players.player.Player;
import com.svintsov.players.misc.ApplicationMode;
import com.svintsov.players.misc.CommandLineException;
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
 * Application entry point. Parses command line and starts either one specific player or both in one process.
 * Command line should contain one argument: -mode. Either sender, receiver or standalone.
 * See {@link ApplicationMode}.
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
        options.addOption(new Option("m", MODE_OPTION, true, "Mode: sender, receiver or standalone"));

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
        if (ApplicationMode.STANDALONE.equals(mode)) {
            new Thread(() -> Player.createWithMode(ApplicationMode.RECEIVER).start()).start();
            new Thread(() -> Player.createWithMode(ApplicationMode.SENDER).start()).start();;
        } else {
            Player.createWithMode(mode).start();
        }
    }

}
