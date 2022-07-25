package com.great.app.repository

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object ApiCredentials {
    const val BASE_URL = "https://gateway.marvel.com/v1/public/"
    val TIME_STAMP = Timestamp(System.currentTimeMillis()).time.toString()
    const val LIMIT = "20"

    fun getApiKey(): String {
        return ApiKeys.PUBLIC_KEY
    }
    fun getHash(): String {
        val input = "$TIME_STAMP${ApiKeys.PRIVATE_KEY}${ApiKeys.PUBLIC_KEY}"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).
        toString(16).padStart(32, '0')
    }
}

object ErrorCode {
    const val UNAUTHORIZED = 401
    const val NOT_FOUND = 404
}