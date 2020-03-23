package com.svintsov.players;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class Server extends Player {

    private Integer messagesReceived = 0;

    @Override
    public ApplicationMode getMode() {
        return ApplicationMode.RECEIVER;
    }

    @Override
    public void start() {
        Try.withResources(() -> new ServerSocket(Application.PORT_NUMBER))
                .of(this::runInternal)
                .onFailure(throwable -> log.error("Error starting server:", throwable));
    }

    private Boolean runInternal(ServerSocket serverSocket) {
        while (!messagesReceived.equals(Application.MAX_MESSAGES)) {
            Try.withResources(serverSocket::accept)
                    .of(this::acceptNext)
                    .onFailure(throwable -> log.error("Error accepting message:", throwable));
        }
        return true;
    }

    private String acceptNext(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        String incomingMessage = Utils.readString(inputStream);
        log.info("Incoming message: {}", incomingMessage);

        String outgoingMessage = String.format("%s #%d", incomingMessage, messagesReceived++);
        IOUtils.write(outgoingMessage, outputStream);
        log.info("Outgoing message: {}", outgoingMessage);

        return outgoingMessage;
    }

}

