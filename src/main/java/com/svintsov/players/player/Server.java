package com.svintsov.players.player;

import com.svintsov.players.Application;
import com.svintsov.players.misc.ApplicationMode;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server-type {@link Player}. Waits for connection to be made from client-type {@link Player}. Then accepts its message and sends confirmation.
 * That happens until {@link Application#MAX_MESSAGES} sent.
 * Every messages happens in new {@link Socket}, another possibility would be to hold socket open and use some kind of pre-defined message terminator.
 * Call {@link #start()} to start sending messages.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class Server extends Player {

    private Integer messagesReceived = 0;

    @Override
    public void start() {
        log.info("starting receiver");
        Try.withResources(() -> new ServerSocket(Application.PORT_NUMBER))
                .of(this::runWithServerSocket)
                .onFailure(throwable -> log.error("Error starting server:", throwable));
    }

    private Boolean runWithServerSocket(ServerSocket serverSocket) {
        while (!messagesReceived.equals(Application.MAX_MESSAGES)) {
            Try.withResources(serverSocket::accept)
                    .of(this::acceptNext)
                    .onFailure(throwable -> log.error("Error accepting message:", throwable));
        }
        return true;
    }

    private String acceptNext(Socket clientSocket) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);

        String incomingMessage = inputReader.readLine();
        log.info("Incoming message: {}", incomingMessage);

        String outgoingMessage = String.format("%s #%d", incomingMessage, messagesReceived++);
        outputWriter.println(outgoingMessage);
        log.info("Outgoing message: {}", outgoingMessage);

        return outgoingMessage;
    }

}

