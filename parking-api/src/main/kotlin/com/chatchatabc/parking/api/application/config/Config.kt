package com.chatchatabc.parking.api.application.config

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.chatchatabc.parking.api.application.config.nats.listener.NatsErrorListener
import io.nats.client.Connection
import io.nats.client.ConnectionListener
import io.nats.client.Nats
import io.nats.client.Options
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config(
    // Aliyun OSS
    @Value("\${aliyun.access.key}")
    private val accessKeyId: String,
    @Value("\${aliyun.access.secret}")
    private val accessKeySecret: String,
    @Value("\${aliyun.oss.endpoint}")
    private val ossEndpoint: String,

    // NATS
    @Value("\${spring.nats.uri}")
    private val natsUri: String,
) {
    private val log = LoggerFactory.getLogger(Config::class.java)

    /**
     * Create a NATS connection
     */
    @Bean
    fun natsConnection(): Connection {
        val options = Options.Builder()
            .server(natsUri)
            .connectionListener { _, events ->
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
        log.info("Connecting to NATS server at $natsUri")
        return Nats.connect(options)
    }

    /**
     * Create an OSS client
     */
    @Bean
    fun ossClient(): OSS {
        return OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret)
    }
}