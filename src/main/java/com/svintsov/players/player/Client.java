package com.svintsov.players.player;

import com.svintsov.players.Application;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client-type {@link Player}. Tries to connect to server-type {@link Player}, send its own message and receive confirmation.
 * That happens until {@link Application#MAX_MESSAGES} sent.
 * Every messages happens in new {@link Socket}, another possibility would be to hold socket open and use some kind of pre-defined message terminator.
 * Call {@link #start()} to start sending messages.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class Client extends Player {

    private Integer messagesSent = 0;

    @Override
    public void start() {
        log.info("starting sender");
        while (!messagesSent.equals(Application.MAX_MESSAGES)) {
            Try.withResources(() -> new Socket("localhost", Application.PORT_NUMBER))
                    .of(this::runWithClientSocket)
                    .onFailure(throwable -> log.error("Error connecting to server: {}", throwable.getMessage()));
        }
    }

    private Boolean runWithClientSocket(Socket clientSocket) {
        Try.of(() -> sendNext(clientSocket))
                .onSuccess(responseMessage -> messagesSent++)
                .onFailure(throwable -> log.error("Error sending message: {}", throwable.getMessage()));
        return true;
    }

    private String sendNext(Socket clientSocket) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);

        String outgoingMessage = String.format("Sending message #%d", messagesSent);
        outputWriter.println(outgoingMessage);
        log.info("Outgoing message: {}", outgoingMessage);

        String incomingMessage = inputReader.readLine();
        log.info("Incoming message: {}", incomingMessage);

        return incomingMessage;
    }

}
