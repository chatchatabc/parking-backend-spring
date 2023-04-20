package com.chatchatabc.service.infra.config.nats.listener

import io.nats.client.Connection
import io.nats.client.ErrorListener
import org.slf4j.LoggerFactory

class NatsErrorListener : ErrorListener {
    private val log = LoggerFactory.getLogger(NatsErrorListener::class.java)

    fun errorOccured(conn: Connection?, exp: Exception?) {
        log.error("The server notificed the client with: $exp")
    }

    fun exceptionOccured(conn: Connection?, exp: Exception?) {
        log.error("The connection handled an exception: ${exp!!.localizedMessage}")
    }

    fun slowConsumerDetected(conn: Connection?, consumer: String?) {
        log.error("The server detected a slow consumer: $consumer")
    }
}