package com.example.quikcartadmin.helpers

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.quikcartadmin.BuildConfig
import com.example.quikcartadmin.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object Constants {
    //base url
    const val BASE_URL = "https://${BuildConfig.API_KEY}:${BuildConfig.PASSWORD}@${BuildConfig.HOSTNAME}/admin/api/${BuildConfig.VERSION}/"


    fun createAlertDialog(context: Context, msg : String): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.loading_dialog)
        return builder.create()
    }

    fun buildDate(strDate : String) : Date?{
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        try {
            return format.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

}