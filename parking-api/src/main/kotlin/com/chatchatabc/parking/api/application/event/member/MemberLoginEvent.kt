package com.chatchatabc.parking.api.application.event.member

import org.springframework.context.ApplicationEvent


class MemberLoginEvent(
    source: Any?,
    val phone: String,
    val otp: String
) : ApplicationEvent(
    source!!
)
