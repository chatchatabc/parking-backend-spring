package com.chatchatabc.parking.api.application.event.user

import org.springframework.context.ApplicationEvent


class UserLoginEvent(
    source: Any?,
    val phone: String,
    val otp: String
) : ApplicationEvent(
    source!!
)
