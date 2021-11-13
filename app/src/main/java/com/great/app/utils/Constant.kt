package com.great.app.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

class Constant {
    companion object {
        const val BASE_URL = "https://gateway.marvel.com/v1/public/"
        val TIME_STAMP = Timestamp(System.currentTimeMillis()).time.toString()

        const val PUBLIC_KEY = "0***3" //0***3
        const val PRIVATE_KEY = "a***9" //a***9
        const val LIMIT = "20"

        fun getApiKey(): String {
            return PUBLIC_KEY
        }
        fun getHash(): String {
            val input = "$TIME_STAMP$PRIVATE_KEY$PUBLIC_KEY"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).
                toString(16).padStart(32, '0')
        }
    }
}