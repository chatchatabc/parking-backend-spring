package com.chatchatabc.parking.infra.config.nats.listener;

import io.nats.client.Connection;
import io.nats.client.Consumer;
import io.nats.client.ErrorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NatsErrorListener implements ErrorListener {
    private final Logger log = LoggerFactory.getLogger(NatsErrorListener.class);

    public void errorOccurred(Connection conn, String error) {
        log.error("The server notificed the client with: " + error);
    }

    public void exceptionOccurred(Connection conn, Exception exp) {
        log.error("The connection handled an exception: " + exp.getLocalizedMessage());
    }

    public void slowConsumerDetected(Connection conn, Consumer consumer) {
        log.error("A slow consumer was detected.");
    }
}