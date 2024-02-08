package com.example.wifi_poc

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.aware.WifiAwareManager
import android.os.Build
import androidx.annotation.RequiresApi

class WifiAwareManagerReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val stateChangedAction = WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED

        if (action == stateChangedAction) {

            val mainActivityIntent = Intent(context, MainActivity::class.java)
            context?.sendBroadcast(mainActivityIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerReceiver(activity: Activity) {
        val filter = IntentFilter(WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED)
        activity.applicationContext.registerReceiver(this, filter)
    }

    fun unregisterReceiver(activity: Activity) {
        activity.applicationContext.unregisterReceiver(this)
    }
}
