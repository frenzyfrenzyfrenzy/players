package com.svintsov.players;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Client.
 *
 * @author Ilya_Svintsov
 */
@Slf4j
public class Client extends Player {

    private Integer messagesSent = 0;

    @Override
    public ApplicationMode getMode() {
        return ApplicationMode.SENDER;
    }

    @Override
    public void start() {
        while (!messagesSent.equals(Application.MAX_MESSAGES)) {
            Try.withResources(() -> new Socket("localhost", Application.PORT_NUMBER))
                    .of(this::runInternal)
                    .onFailure(throwable -> log.error("Error opening client socket: {}", throwable.getMessage()));
        }
    }

    private Boolean runInternal(Socket clientSocket) {
        Try.of(() -> sendNext(clientSocket))
                .onSuccess(responseMessage -> messagesSent++)
                .onFailure(throwable -> log.error("Error sending message: {}", throwable.getMessage()));
        return true;
    }

    private String sendNext(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        String outgoingMessage = String.format("Sending message #%d", messagesSent);
        IOUtils.write(outgoingMessage, outputStream, "UTF-8");
        log.info("Outgoing message: {}", outgoingMessage);

        String incomingMessage = Utils.readString(inputStream);
        log.info("Incoming message: {}", incomingMessage);

        return incomingMessage;
    }

}
