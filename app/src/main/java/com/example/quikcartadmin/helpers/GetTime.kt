package com.example.quikcartadmin.helpers

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object GetTime {
    fun getCurrentTime(): String {
        val currentTime = System.currentTimeMillis()
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")
        return outputFormat.format(currentTime)
    }


    fun formatDateString(dateString: String?, format: String = "yyyy-MM-dd HH:mm"): String {
        if (dateString.isNullOrEmpty()) return ""
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()) // Adjusted for timezone offset

        val outputFormat = SimpleDateFormat(format, Locale.getDefault())
        return try {
            val date: Date? = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }

}
