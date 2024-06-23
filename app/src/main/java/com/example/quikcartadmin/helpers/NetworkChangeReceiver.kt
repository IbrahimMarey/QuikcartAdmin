package com.example.quikcartadmin.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class NetworkChangeReceiver(private val activity: AppCompatActivity) : BroadcastReceiver() {
    private var alertDialog: AlertDialog? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = conn.activeNetworkInfo
            if (networkInfo == null || !networkInfo.isAvailable || !networkInfo.isConnected) {
                showNoNetworkAlert()
            } else {
                dismissNoNetworkAlert()
            }
        }
    }

    private fun showNoNetworkAlert() {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(activity)
                .setTitle("No Network Connection")
                .setMessage("A network connection is required to use this app. Please check your network settings and try again.")
                .setPositiveButton("Ok") { _, _ -> activity.finish() }
                .setCancelable(false)
                .create()
            alertDialog?.show()
        }
    }

    private fun dismissNoNetworkAlert() {
        alertDialog?.dismiss()
        alertDialog = null
    }
}
