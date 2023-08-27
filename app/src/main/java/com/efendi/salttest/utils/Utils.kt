package com.efendi.salttest.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Utils {
    class Constants {
        companion object {
            const val QUERY_PER_PAGE = 10
            const val BASE_URL = "https://newsapi.org/"
//            const val apiKey = "0951235897ef4c7db9a703b7246d2413"
//            const val apiKey = "d91fc22b6eb44acebae9e7e3fb28d1e8"
            const val apiKey = "dd6d4e22d4ad429cbe5fa7f937e6c1be"
            const val country = "us"
        }
    }



    companion object {
        fun formatIndonesianTime(time: String): CharSequence? {

            // Mengonversi string ke Instant
            val instant = Instant.parse(time)

            // Mengonversi Instant ke LocalDateTime
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            // Memformat LocalDateTime ke format Indonesia
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")
            return localDateTime.format(formatter)
        }
    }
}