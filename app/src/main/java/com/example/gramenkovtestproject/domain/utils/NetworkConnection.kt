package com.example.gramenkovtestproject.domain.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.gramenkovtestproject.App.Companion.ctx

object NetworkConnection {
    @Suppress("DEPRECATION")
    fun isInternetAvailable(): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}