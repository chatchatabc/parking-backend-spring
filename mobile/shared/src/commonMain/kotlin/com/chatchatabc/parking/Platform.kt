package com.chatchatabc.parking

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform