package com.example.quikcartadmin.helpers

import com.example.quikcartadmin.BuildConfig

object Constants {
    //base url
    const val BASE_URL = "https://${BuildConfig.API_KEY}:${BuildConfig.PASSWORD}@${BuildConfig.HOSTNAME}/admin/api/${BuildConfig.VERSION}/"

}