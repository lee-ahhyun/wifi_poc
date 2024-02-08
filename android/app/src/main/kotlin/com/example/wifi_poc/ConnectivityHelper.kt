package com.example.wifi_poc

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService

class ConnectivityHelper(private val context:Context){

  private val networkCallBack = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.d("onAvailable","network"+network)
        }

        override fun onLost(network: Network) {
            Log.d("onLost","")
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
     fun registerNetworkCallback() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallBack)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
     fun terminateNetworkCallback() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }

    fun getActiveNetworkInfo(): NetworkInfo? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Log.d("getActiveNetworkInfo",connectivityManager.activeNetworkInfo.toString())

        return connectivityManager.activeNetworkInfo
    }
}