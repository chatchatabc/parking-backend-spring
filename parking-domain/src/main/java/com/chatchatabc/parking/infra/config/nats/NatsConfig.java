package com.chatchatabc.parking.infra.config.nats;

import com.chatchatabc.parking.infra.config.nats.listener.NatsErrorListener;
import io.nats.client.Connection;
import io.nats.client.ConnectionListener;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class NatsConfig {
    // URL to connect to NATS server
    @Value("${spring.nats.uri}")
    private String natsURI;

    private final Logger log = LoggerFactory.getLogger(NatsConfig.class);

    @Bean
    public Connection natsConnection() throws IOException, InterruptedException {
        // Nats config
        Options options = new Options.Builder()
                .server(natsURI)
                .connectionListener((connection, events) -> {
                    if (events == ConnectionListener.Events.DISCONNECTED) {
                        log.info("Disconnected from NATS server");
                    } else if (events == ConnectionListener.Events.RECONNECTED) {
                        log.info("Reconnected to NATS server");
                    } else if (events == ConnectionListener.Events.CLOSED) {
                        log.info("Closed connection to NATS server");
                    } else if (events == ConnectionListener.Events.CONNECTED) {
                        log.info("Connected to NATS server");
                    }
                })
                .errorListener(new NatsErrorListener())
                .build();
        // Create NATS connection
        log.info("Connecting to NATS server at " + natsURI);
        return Nats.connect(options);
    }
}