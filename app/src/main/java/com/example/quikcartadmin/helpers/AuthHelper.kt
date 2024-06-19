package com.example.quikcartadmin.helpers

import android.content.Context
import android.content.SharedPreferences

object AuthHelper {
    private const val PREFS_NAME = "quikcart_prefs"
    private const val KEY_IS_SIGNED_IN = "is_signed_in"

    fun isUserSignedIn(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_SIGNED_IN, false)
    }

    fun setUserSignedIn(context: Context, isSignedIn: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(KEY_IS_SIGNED_IN, isSignedIn)
            apply()
        }
    }
}