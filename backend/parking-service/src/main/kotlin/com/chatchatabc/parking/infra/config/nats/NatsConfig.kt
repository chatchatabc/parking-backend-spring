package com.chatchatabc.parking.infra.config.nats

import com.chatchatabc.parking.infra.config.nats.listener.NatsErrorListener
import io.nats.client.Connection
import io.nats.client.ConnectionListener
import io.nats.client.Nats
import io.nats.client.Options
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NatsConfig(
    @Value("\${server.nats.uri}")
    private val natsURI: String,
) {
    private val log = LoggerFactory.getLogger(NatsConfig::class.java)

    @Bean
    fun natsConnection(): Connection {
        val options = Options.Builder()
            .server(natsURI)
            .connectionListener { connection, events ->
                when (events) {
                    ConnectionListener.Events.DISCONNECTED -> {
                        log.info("Disconnected from NATS server")
                    }

                    ConnectionListener.Events.RECONNECTED -> {
                        log.info("Reconnected to NATS server")
                    }

                    ConnectionListener.Events.CLOSED -> {
                        log.info("Closed connection to NATS server")
                    }

                    ConnectionListener.Events.CONNECTED -> {
                        log.info("Connected to NATS server")
                    }

                    else -> {
                        log.info("Unknown event: $events")
                    }
                }
            }
            .errorListener(NatsErrorListener())
            .build()
        log.info("Connecting to NATS server at $natsURI")
        return Nats.connect(options)
    }
}